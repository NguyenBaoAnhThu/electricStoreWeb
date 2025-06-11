package org.example.electricstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.electricstore.DTO.warehouse.PaymentHistoryDTO;
import org.example.electricstore.exception.invoice.InvoiceException;
import org.example.electricstore.model.*;
import org.example.electricstore.repository.InvoiceRepository;
import org.example.electricstore.repository.InvoiceItemRepository;
import org.example.electricstore.repository.PaymentHistoryRepository;
import org.example.electricstore.service.common.PDFImportService;
import org.example.electricstore.service.impl.InvoiceService;
import org.example.electricstore.service.impl.ProductService;
import org.example.electricstore.service.impl.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Admin/invoice")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InvoiceRestController {

    private final InvoiceRepository invoiceRepo;
    private final InvoiceItemRepository invoiceItemRepo;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PDFImportService pdfImportService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) {
        try {
            if (invoice.getSupplier() == null && invoice.getSupplierId() != null) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(invoice.getSupplierId());
                invoice.setSupplier(supplier);
            }

            invoiceService.validateInvoice(invoice);

            List<InvoiceItem> items = invoice.getProducts();
            if (items != null) {
                for (InvoiceItem item : items) {
                    item.setInvoice(invoice);
                }
            }

            invoice.calculateTotalPrice();
            System.out.println("Before saving: totalPrice = " + invoice.getTotalPrice());

            Invoice savedInvoice = invoiceRepo.save(invoice);
            System.out.println("After saving: totalPrice = " + savedInvoice.getTotalPrice());

            if (items != null) {
                for (InvoiceItem item : items) {
                    updateProductStock(item.getProductId().intValue(), (int)item.getQuantity());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lưu hóa đơn thành công!");
            response.put("invoiceId", savedInvoice.getId());
            response.put("totalPrice", savedInvoice.getTotalPrice());

            return ResponseEntity.ok(response);
        } catch (InvoiceException ex) {
            Map<String, String> error = new HashMap<>();
            switch (ex.getErrorCode()) {
                case FUTURE_IMPORT_DATE:
                    error.put("importDate", ex.getMessage());
                    break;
                case SUPPLIER_REQUIRED:
                    error.put("supplierId", ex.getMessage());
                    break;
                default:
                    error.put("globalError", ex.getMessage());
            }
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống khi lưu hóa đơn: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelInvoice(
            @RequestParam Integer invoiceId,
            @RequestParam String reason) {
        try {
            Invoice invoice = invoiceRepo.findByIdWithProducts(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

            invoice.setCancelReason(reason);

            for (InvoiceItem item : invoice.getProducts()) {
                if (!"ĐÃ HỦY".equals(item.getPaymentStatus())) {
                    updateProductStock(item.getProductId().intValue(), -(int)item.getQuantity());
                    item.setPaymentStatus("ĐÃ HỦY");
                }
            }

            invoiceRepo.save(invoice);

            return ResponseEntity.ok("Đã hủy phiếu nhập thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi hủy phiếu nhập: " + e.getMessage());
        }
    }

    private void updateProductStock(int productId, int quantity) {
        Product product = productService.findById(productId);
        if (product != null) {
            Integer currentStock = product.getStock() != null ? product.getStock() : 0;
            int newStock = Math.max(0, currentStock + quantity);
            product.setStock(newStock);
            productService.saveProduct(product);
        }
    }

    @GetMapping("/export-pdf/{invoiceId}")
    public ResponseEntity<byte[]> exportPDF(@PathVariable Integer invoiceId) {
        try {
            byte[] pdfBytes = pdfImportService.createImportPDF(invoiceId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payment-process")
    public ResponseEntity<?> processPayment(
            @RequestParam Integer invoiceId,
            @RequestParam String method,
            @RequestParam Double amount) {
        try {
            Optional<Invoice> invoiceOpt = invoiceRepo.findByIdWithProducts(invoiceId);
            if (invoiceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy phiếu nhập với ID: " + invoiceId);
            }

            Invoice invoice = invoiceOpt.get();

            invoice.calculateTotalPrice();
            invoiceRepo.save(invoice);

            double totalAmount = invoice.getTotalPrice();
            double previouslyPaid = paymentHistoryRepository.sumAmountByInvoiceId(invoiceId);
            double amountDue = totalAmount - previouslyPaid;
            double currentPaymentAmount = amount;

            if (currentPaymentAmount <= 0 || currentPaymentAmount > amountDue) {
                return ResponseEntity.badRequest()
                        .body("Số tiền thanh toán không hợp lệ: " + currentPaymentAmount);
            }

            PaymentHistory paymentHistory = PaymentHistory.builder()
                    .invoiceId(invoiceId)
                    .amount(currentPaymentAmount)
                    .method(getPaymentMethodText(method))
                    .paidAt(LocalDateTime.now())
                    .build();
            paymentHistoryRepository.save(paymentHistory);

            // Update payment status
            double totalPaid = previouslyPaid + currentPaymentAmount;
            String newStatus;
            if (totalPaid >= totalAmount) {
                newStatus = "ĐÃ THANH TOÁN";
            } else if (totalPaid > 0) {
                newStatus = "ĐANG TIẾN HÀNH";
            } else {
                newStatus = "CHỜ THANH TOÁN";
            }

            for (InvoiceItem item : invoice.getProducts()) {
                item.setPaymentStatus(newStatus);
            }
            invoiceRepo.save(invoice);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thanh toán thành công");
            response.put("paidAmount", currentPaymentAmount);
            response.put("totalAmount", totalAmount);
            response.put("totalPaid", totalPaid);
            response.put("amountDue", Math.max(0, amountDue - currentPaymentAmount));
            response.put("status", newStatus);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xử lý thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory(@RequestParam Integer invoiceId) {
        try {
            List<PaymentHistory> historyList = paymentHistoryRepository.findByInvoiceIdOrderByPaidAtDesc(invoiceId);
            List<PaymentHistoryDTO> dtoList = historyList.stream()
                    .map(PaymentHistoryDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy dữ liệu lịch sử thanh toán");
        }
    }

    private String getPaymentMethodText(String method) {
        switch (method) {
            case "cash": return "Tiền mặt";
            case "bank": return "Chuyển khoản";
            case "card": return "Quẹt thẻ";
            default: return "Không xác định";
        }
    }

    @GetMapping("/next-code")
    public String getNextInvoiceCode() {
        return invoiceService.getNextInvoiceCode();
    }

    @GetMapping("/create-import")
    public String showCreateImportForm(Model model, @RequestParam(required = false) Integer supplierId) {
        String nextReceiptCode = invoiceService.getNextInvoiceCode();
        model.addAttribute("receiptCode", nextReceiptCode);
        model.addAttribute("importDate", LocalDate.now());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("products", productService.getAllProducts());
        if (supplierId != null) {
            model.addAttribute("selectedSupplier", supplierId);
        }
        return "admin/warehouse/createImport";
    }
}