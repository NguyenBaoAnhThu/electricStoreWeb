package org.example.electricstore.service.impl;

import org.example.electricstore.exception.invoice.InvoiceError;
import org.example.electricstore.exception.invoice.InvoiceException;
import org.example.electricstore.model.Invoice;
import org.example.electricstore.model.InvoiceItem;
import org.example.electricstore.model.Product;
import org.example.electricstore.repository.InvoiceRepository;
import org.example.electricstore.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        String lastInvoiceCode = invoiceRepository.findLatestReceiptCode();
        if (lastInvoiceCode == null) {
            lastInvoiceCode = "NK0000";
        }

        String prefix = "NK";
        String numberPart = lastInvoiceCode.substring(prefix.length());

        int number;
        try {
            number = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            number = 0;
        }
        number++;

        return prefix + String.format("%04d", number);
    }

    public void validateInvoice(Invoice invoice) {
        if (invoice.getImportDate() == null) {
            throw new RuntimeException("Ngày nhập không được để trống");
        }

        LocalDate today = LocalDate.now();
        if (invoice.getImportDate().isAfter(today)) {
            throw new InvoiceException(InvoiceError.FUTURE_IMPORT_DATE);
        }

        if (invoice.getSupplier() == null && invoice.getSupplierId() == null) {
            throw new InvoiceException(InvoiceError.SUPPLIER_REQUIRED);
        }
    }

    @Transactional
    public Invoice saveInvoice(Invoice invoice) {
        validateInvoice(invoice);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        updateProductStock(savedInvoice);

        return savedInvoice;
    }

    private void updateProductStock(Invoice invoice) {
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (InvoiceItem item : invoice.getProducts()) {
                Optional<Product> productOpt = productRepository.findById(item.getProductId().intValue());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();

                    Integer currentStock = product.getStock();
                    if (currentStock == null) {
                        currentStock = 0;
                    }
                    product.setStock(currentStock + (int)item.getQuantity());
                    productRepository.save(product);
                }
            }
        }
    }

    @Transactional
    public boolean cancelInvoice(Integer invoiceId, String reason) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (!invoiceOpt.isPresent()) {
            return false;
        }

        Invoice invoice = invoiceOpt.get();
        invoice.setCancelReason(reason);
        reverseProductStock(invoice);
        invoiceRepository.save(invoice);

        return true;
    }

    private void reverseProductStock(Invoice invoice) {
        if (invoice.getProducts() != null && !invoice.getProducts().isEmpty()) {
            for (InvoiceItem item : invoice.getProducts()) {
                Optional<Product> productOpt = productRepository.findById(item.getProductId().intValue());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();

                    Integer currentStock = product.getStock();
                    if (currentStock == null) {
                        currentStock = 0;
                    }

                    int newStock = currentStock - (int)item.getQuantity();
                    if (newStock < 0) {
                        newStock = 0;
                    }

                    product.setStock(newStock);

                    productRepository.save(product);
                }
            }
        }
    }

    public Optional<Invoice> getInvoiceById(Integer id) {
        return invoiceRepository.findById(id);
    }
}