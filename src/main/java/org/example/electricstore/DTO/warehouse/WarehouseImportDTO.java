package org.example.electricstore.DTO.warehouse;

import lombok.Data;
import org.example.electricstore.exception.invoice.InvoiceError;
import org.example.electricstore.exception.invoice.InvoiceException;

import java.time.LocalDate;
import java.util.List;

@Data
public class WarehouseImportDTO {
    private Integer id;
    private String receiptCode;
    private LocalDate importDate;
    private Integer supplierId;
    private String supplierName;
    private String notes;
    private double discount;
    private double vat;
    private double additionalFees;
    private List<InvoiceItemDTO> products;
    private double totalAmount;
    private double grandTotal;
    private long totalQuantity;

    public void validate() {
        validateImportDate();
        validateSupplier();
    }

    private void validateImportDate() {
        if (importDate == null) {
            throw new RuntimeException("Ngày nhập không được để trống");
        }

        LocalDate today = LocalDate.now();
        if (importDate.isAfter(today)) {
            throw new InvoiceException(InvoiceError.FUTURE_IMPORT_DATE);
        }
    }
    private void validateSupplier() {
        if (supplierId == null) {
            throw new InvoiceException(InvoiceError.SUPPLIER_REQUIRED);
        }
    }
}