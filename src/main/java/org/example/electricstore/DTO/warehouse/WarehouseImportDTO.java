package org.example.electricstore.DTO.warehouse;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WarehouseImportDTO {
    private Long id;
    private String receiptCode;
    private LocalDate importDate;
    private String supplierId;
    private String supplierName; // Để hiển thị tên nhà cung cấp trên PDF
    private String notes;
    private long discount;
    private double vat;
    private long additionalFees;
    private List<InvoiceItemDTO> products;
    private long totalAmount; // Tổng tiền hàng
    private long grandTotal; // Tổng giá trị nhập (sau khi tính thuế, chiết khấu, phí)
    private long totalQuantity; // Tổng số lượng nhập
}