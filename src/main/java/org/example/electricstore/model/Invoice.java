package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiptCode;

    private LocalDate importDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Transient
    private Integer supplierId;

    private String notes;

    private long discount;

    private double vat;

    private long additionalFees;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> products;

    public Integer getSupplierId() {
        return supplierId != null ? supplierId : (supplier != null ? supplier.getSupplierID() : null);
    }

    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId;}
}