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
    private double vat;

    @Column(name = "additional_fees")
    private long additionalFees;

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

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
}