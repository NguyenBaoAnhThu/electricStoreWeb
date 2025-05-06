package org.example.electricstore.repository;

import org.example.electricstore.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Integer> {
    void deleteBySupplierIDIn(List<Integer> ids);

    @Query("SELECT MAX(s.supplierCode) FROM Supplier s WHERE s.supplierCode LIKE 'NCC%'")
    String findMaxSupplierCode();

    @Query("SELECT s FROM Supplier s WHERE " +
            "(:supplierCode IS NULL OR s.supplierCode LIKE %:supplierCode%) AND " +
            "(:supplierName IS NULL OR s.supplierName LIKE %:supplierName%) AND " +
            "(:address IS NULL OR s.address LIKE %:address%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%) AND " +
            "(:email IS NULL OR s.email LIKE %:email%)")
    Page<Supplier> searchByFields(
            String supplierCode,
            String supplierName,
            String address,
            String phone,
            String email,
            Pageable pageable);
}