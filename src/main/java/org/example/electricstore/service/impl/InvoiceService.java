package org.example.electricstore.service.impl;

import org.example.electricstore.model.Invoice;
import org.example.electricstore.model.InvoiceItem;
import org.example.electricstore.model.Product;
import org.example.electricstore.repository.InvoiceRepository;
import org.example.electricstore.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public String getNextInvoiceCode() {
        // Lấy mã phiếu nhập kho lớn nhất hiện tại
        String lastInvoiceCode = invoiceRepository.findLatestReceiptCode();
        if (lastInvoiceCode == null) {
            lastInvoiceCode = "NK0000";
        }

        // Tách phần số từ mã phiếu nhập
        String prefix = "NK";
        String numberPart = lastInvoiceCode.substring(prefix.length());

        // Tăng số lên 1
        int number;
        try {
            number = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            // Nếu không thể parse thành số, bắt đầu lại từ 0
            number = 0;
        }
        number++;

        // Format lại số với số 0 đứng trước (4 chữ số)
        return prefix + String.format("%04d", number);
    }

    @Transactional
    public Invoice saveInvoice(Invoice invoice) {
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Cập nhật số lượng tồn kho cho các sản phẩm
        updateProductStock(savedInvoice);

        return savedInvoice;
    }

    private void updateProductStock(Invoice invoice) {
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (InvoiceItem item : invoice.getProducts()) {
                Optional<Product> productOpt = productRepository.findById(item.getProductId().intValue());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();

                    // Lấy số lượng tồn kho hiện tại
                    Integer currentStock = product.getStock();
                    if (currentStock == null) {
                        currentStock = 0;
                    }

                    // Tăng số lượng tồn kho
                    product.setStock(currentStock + (int)item.getQuantity());

                    // Lưu sản phẩm đã cập nhật
                    productRepository.save(product);
                }
            }
        }
    }

    @Transactional
    public boolean cancelInvoice(Long invoiceId, String reason) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (!invoiceOpt.isPresent()) {
            return false;
        }

        Invoice invoice = invoiceOpt.get();

        // Cập nhật lý do hủy
        invoice.setCancelReason(reason);

        // Giảm số lượng tồn kho của các sản phẩm
        reverseProductStock(invoice);

        // Lưu phiếu nhập đã hủy
        invoiceRepository.save(invoice);

        return true;
    }

    private void reverseProductStock(Invoice invoice) {
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (InvoiceItem item : invoice.getProducts()) {
                Optional<Product> productOpt = productRepository.findById(item.getProductId().intValue());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();

                    // Lấy số lượng tồn kho hiện tại
                    Integer currentStock = product.getStock();
                    if (currentStock == null) {
                        currentStock = 0;
                    }

                    // Giảm số lượng tồn kho
                    int newStock = currentStock - (int)item.getQuantity();
                    // Đảm bảo số lượng không âm
                    if (newStock < 0) {
                        newStock = 0;
                    }

                    product.setStock(newStock);

                    // Lưu sản phẩm đã cập nhật
                    productRepository.save(product);
                }
            }
        }
    }

    // Thêm phương thức để lấy phiếu nhập theo id
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    // Các phương thức khác trong service
}