package org.example.electricstore.repository;

import org.example.electricstore.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    List<PaymentHistory> findByInvoiceIdOrderByPaidAtDesc(Long invoiceId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentHistory p WHERE p.invoiceId = :invoiceId")
    long sumAmountByInvoiceId(@Param("invoiceId") Long invoiceId);
}