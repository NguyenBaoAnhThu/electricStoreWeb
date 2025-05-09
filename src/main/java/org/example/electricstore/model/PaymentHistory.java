package org.example.electricstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Invoice invoice;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;
}