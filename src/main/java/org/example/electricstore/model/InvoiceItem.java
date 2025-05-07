package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_item")
@Getter
@Setter
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_code", nullable = false, length = 255)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "brand", nullable = false, length = 255)
    private String brand;

    @Column(name = "quantity", nullable = false)
    private double quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "payment_status", nullable = false, length = 255)
    private String paymentStatus = "CHỜ THANH TOÁN";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}