package org.example.electricstore.repository;

import org.example.electricstore.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.products LEFT JOIN FETCH i.supplier WHERE i.id = :id")
    Optional<Invoice> findByIdWithProducts(@Param("id") Integer id);

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.supplier WHERE i.id = :id")
    Optional<Invoice> findByIdWithSupplier(@Param("id") Integer id);

    @Query("SELECT MAX(i.receiptCode) FROM Invoice i WHERE i.receiptCode LIKE 'NK%'")
    String findLatestReceiptCode();

    boolean existsByReceiptCode(String receiptCode);

    @Query("SELECT i FROM Invoice i LEFT JOIN i.supplier s WHERE " +
            "(:code IS NULL OR i.receiptCode LIKE %:code%) AND " +
            "(:brand IS NULL OR (s.supplierName LIKE %:brand% OR s.supplierCode LIKE %:brand%)) AND " +
            "(:user IS NULL OR 'Admin' LIKE %:user%) AND " +
            "(:fromDate IS NULL OR i.importDate >= CAST(:fromDate AS date)) AND " +
            "(:toDate IS NULL OR i.importDate <= CAST(:toDate AS date))")
    Page<Invoice> findWithFilters(
            @Param("code") String code,
            @Param("brand") String brand,
            @Param("user") String user,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable);

    Optional<Invoice> findTopByOrderByIdDesc();
}