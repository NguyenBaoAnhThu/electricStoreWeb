package org.example.electricstore.controller.admin;

import lombok.RequiredArgsConstructor;
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

    // In InvoiceRestController.java
    @PostMapping
    public ResponseEntity<String> saveInvoice(@RequestBody Invoice invoice) {
        try {
            // Check if supplierId exists in the request
            if (invoice.getSupplier() == null && invoice.getSupplierId() != null) {
                // Create a reference Supplier with just the ID
                Supplier supplier = new Supplier();
                supplier.setSupplierID(invoice.getSupplierId());
                invoice.setSupplier(supplier);
            }

            invoiceRepo.save(invoice); // Save invoice to get ID

            List<InvoiceItem> items = invoice.getProducts();
            if (items != null) {
                for (InvoiceItem item : items) {
                    item.setInvoice(invoice); // Set back invoice after getting ID

                    // Tăng số lượng tồn kho cho mỗi sản phẩm
                    updateProductStock(item.getProductId().intValue(), (int)item.getQuantity());
                }
                invoiceItemRepo.saveAll(items);
            }
            return ResponseEntity.ok("Lưu hóa đơn thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống khi lưu hóa đơn: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelInvoice(
            @RequestParam Integer invoiceId,
            @RequestParam String reason) {
        try {
            Invoice invoice = invoiceRepo.findByIdWithProducts(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

            // Lưu lý do hủy phiếu
            invoice.setCancelReason(reason);

            // Trước tiên, giảm số lượng tồn kho cho tất cả sản phẩm
            for (InvoiceItem item : invoice.getProducts()) {
                // Kiểm tra nếu trạng thái hiện tại không phải "ĐÃ HỦY"
                if (!"ĐÃ HỦY".equals(item.getPaymentStatus())) {
                    // Giảm số lượng tồn kho
                    updateProductStock(item.getProductId().intValue(), -(int)item.getQuantity());
                }
            }

            // Sau đó, mới cập nhật trạng thái
            for (InvoiceItem item : invoice.getProducts()) {
                item.setPaymentStatus("ĐÃ HỦY");
            }

            // Lưu những thay đổi vào cơ sở dữ liệu
            invoiceRepo.save(invoice);

            return ResponseEntity.ok("Đã hủy phiếu nhập thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi hủy phiếu nhập: " + e.getMessage());
        }
    }

    /**
     * Cập nhật số lượng tồn kho của sản phẩm
     * @param productId ID của sản phẩm
     * @param quantity Số lượng thay đổi (dương: tăng, âm: giảm)
     */
    private void updateProductStock(int productId, int quantity) {
        Product product = productService.findById(productId);
        if (product != null) {
            // Lấy số lượng tồn kho hiện tại
            Integer currentStock = product.getStock();
            if (currentStock == null) {
                currentStock = 0;
            }

            // Tính toán số lượng mới
            int newStock = currentStock + quantity;
            // Đảm bảo tồn kho không âm
            if (newStock < 0) {
                newStock = 0;
            }

            // Cập nhật tồn kho
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
            headers.setContentDispositionFormData("attachment", "phieu-nhap-" + invoiceId + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API endpoint để xử lý thanh toán
    @PostMapping("/payment-process")
    public ResponseEntity<?> processPayment(
            @RequestParam Integer invoiceId,
            @RequestParam String method,
            @RequestParam Double amount) {

        try {
            // Tìm invoice theo ID
            Optional<Invoice> invoiceOpt = invoiceRepo.findByIdWithProducts(invoiceId);

            if (invoiceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy phiếu nhập với ID: " + invoiceId);
            }

            Invoice invoice = invoiceOpt.get();

            // Tính tổng tiền cần thanh toán
            double totalAmount = 0;
            for (InvoiceItem item : invoice.getProducts()) {
                totalAmount += item.getQuantity() * item.getPrice();
            }

            // Trừ giảm giá và cộng phí khác
            totalAmount -= invoice.getDiscount();
            totalAmount += invoice.getAdditionalFees();

            // Tính tổng tiền đã thanh toán từ lịch sử
            double previouslyPaid = paymentHistoryRepository.sumAmountByInvoiceId(invoiceId);

            // Tính toán số tiền còn nợ
            double amountDue = totalAmount - previouslyPaid;

            // Số tiền đang thanh toán
            double currentPaymentAmount = amount;

            // Lưu lịch sử thanh toán
            PaymentHistory paymentHistory = PaymentHistory.builder()
                    .invoiceId(invoiceId)
                    .amount(currentPaymentAmount)
                    .method(getPaymentMethodText(method))
                    .paidAt(LocalDateTime.now())
                    .build();

            paymentHistoryRepository.save(paymentHistory);

            // Tổng tiền đã thanh toán sau đợt này
            double totalPaid = previouslyPaid + currentPaymentAmount;

            // Cập nhật trạng thái thanh toán
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Total Paid: " + totalPaid);

            if (totalPaid >= totalAmount) {
                System.out.println("Setting status to ĐÃ THANH TOÁN");
                for (InvoiceItem item : invoice.getProducts()) {
                    item.setPaymentStatus("ĐÃ THANH TOÁN");
                }
            } else if (totalPaid > 0) {
                System.out.println("Setting status to ĐANG TIẾN HÀNH");
                // Nếu đã thanh toán một phần
                for (InvoiceItem item : invoice.getProducts()) {
                    item.setPaymentStatus("ĐANG TIẾN HÀNH");
                }
            } else {
                System.out.println("Setting status to CHỜ THANH TOÁN");
                for (InvoiceItem item : invoice.getProducts()) {
                    item.setPaymentStatus("CHỜ THANH TOÁN");
                }
            }

            // Lưu invoice sau khi cập nhật
            invoiceRepo.save(invoice);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thanh toán thành công");
            response.put("paidAmount", currentPaymentAmount);
            response.put("totalAmount", totalAmount);
            response.put("totalPaid", totalPaid);
            response.put("amountDue", Math.max(0, amountDue - currentPaymentAmount));
            response.put("status", invoice.getProducts().get(0).getPaymentStatus());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xử lý thanh toán: " + e.getMessage());
        }
    }

    // API endpoint để lấy lịch sử thanh toán
    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory(@RequestParam Integer invoiceId) {
        try {
            List<PaymentHistory> historyList = paymentHistoryRepository.findByInvoiceIdOrderByPaidAtDesc(invoiceId);
            return ResponseEntity.ok(historyList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy dữ liệu lịch sử thanh toán");
        }
    }

    private String getPaymentMethodText(String method) {
        switch (method) {
            case "cash":
                return "Tiền mặt";
            case "bank":
                return "Chuyển khoản";
            case "card":
                return "Quẹt thẻ";
            default:
                return "Không xác định";
        }
    }


    @GetMapping("/next-code")
    @ResponseBody
    public String getNextInvoiceCode() {
        return invoiceService.getNextInvoiceCode();
    }

    @GetMapping("/create-import")
    public String showCreateImportForm(Model model, @RequestParam(required = false) Integer supplierId) {
        // Lấy mã phiếu nhập tự động tăng
        String nextReceiptCode = invoiceService.getNextInvoiceCode();

        // Thêm vào model
        model.addAttribute("receiptCode", nextReceiptCode);

        // Thêm ngày hiện tại làm mặc định
        model.addAttribute("importDate", LocalDate.now().toString());

        // Lấy danh sách nhà cung cấp và sản phẩm (code hiện có)
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("products", productService.getAllProducts());

        // Lưu supplier đã chọn (nếu có)
        if (supplierId != null) {
            model.addAttribute("selectedSupplier", supplierId);
        }

        return "admin/warehouse/createImport";
    }
}