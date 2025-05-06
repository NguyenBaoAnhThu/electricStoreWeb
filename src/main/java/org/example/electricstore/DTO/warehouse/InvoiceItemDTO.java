package org.example.electricstore.DTO.warehouse;

import lombok.Data;

@Data
public class InvoiceItemDTO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String brand;
    private long quantity;
    private long price;
    private String paymentStatus;
    private long total; // Thành tiền của sản phẩm
}