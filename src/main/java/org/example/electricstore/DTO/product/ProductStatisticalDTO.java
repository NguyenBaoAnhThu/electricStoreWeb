package org.example.electricstore.DTO.product;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatisticalDTO {
    private String productCode;
    private Integer productId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String supplierName;
    private Double price;
    private Long stock;
}
