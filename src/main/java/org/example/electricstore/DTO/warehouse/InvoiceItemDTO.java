package org.example.electricstore.DTO.warehouse;

import lombok.Data;

@Data
public class InvoiceItemDTO {
    private Integer id;
    private Integer productId;
    private String productCode;
    private String productName;
    private String brand;
    private double quantity;
    private double price;
    private String paymentStatus;
    private double total; // Thành tiền của sản phẩm
}