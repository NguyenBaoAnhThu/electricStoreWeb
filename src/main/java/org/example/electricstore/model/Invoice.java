package org.example.electricstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", nullable = false)
    private Integer id;

    @Column(name = "invoice_code", nullable = false, length = 255)
    private String receiptCode;

    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Transient
    private Integer supplierId;

    @Column(length = 255)
    private String notes;

    @Column
    private double discount;

    @Column
    private double vat; // Stored as a percentage (e.g., 10.0 for 10%)

    @Column(name = "additional_fees")
    private double additionalFees; // Changed to double for consistency with other monetary fields

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> products;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PaymentHistory> paymentHistories;

    public Integer getSupplierId() {
        return supplierId != null ? supplierId : (supplier != null ? supplier.getSupplierID() : null);
    }

    // Phương thức tính totalPrice theo công thức: (tổng quantity * price) - discount + VAT (trước discount) + additionalFees
    public void calculateTotalPrice() {
        if (products == null || products.isEmpty()) {
            this.totalPrice = 0.0;
            return;
        }

        // Tính tổng giá trị sản phẩm (quantity * price)
        double productTotal = products.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();

        // Tính VAT dựa trên tổng giá trị sản phẩm trước khi trừ discount
        double vatAmount = (productTotal * vat) / 100; // Convert percentage to decimal

        // Tính totalPrice: (tổng giá trị sản phẩm) - discount + VAT + additionalFees
        this.totalPrice = productTotal - discount + vatAmount + additionalFees;

        // Ensure totalPrice is not negative
        if (this.totalPrice < 0) {
            this.totalPrice = 0.0;
        }
    }
}