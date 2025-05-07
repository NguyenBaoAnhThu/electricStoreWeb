package org.example.electricstore.DTO.warehouse;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WarehouseImportDTO {
    private Integer id;
    private String receiptCode;
    private LocalDate importDate;
    private String supplierId;
    private String supplierName;
    private String notes;
    private double discount;
    private double vat;
    private double additionalFees;
    private List<InvoiceItemDTO> products;
    private double totalAmount;
    private double grandTotal;
    private long totalQuantity;
}