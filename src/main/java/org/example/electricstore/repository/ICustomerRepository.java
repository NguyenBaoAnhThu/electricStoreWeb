package org.example.electricstore.repository;

import org.example.electricstore.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Page<Customer> findByCustomerId(int customerId, Pageable pageable);

    Page<Customer> findByCustomerNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Customer> findByCustomerCodeContainingIgnoreCase(String code, Pageable pageable);
    Page<Customer> findByPhoneNumberContaining(String phone, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE " +
            "(:field = 'customerName' AND c.customerName LIKE %:keyword%) OR " +
            "(:field = 'customerCode' AND c.customerCode LIKE %:keyword%) OR " +
            "(:field = 'phoneNumber' AND c.phoneNumber LIKE %:keyword%)")
    Page<Customer> searchCustomers(@Param("field") String field,
                                   @Param("keyword") String keyword,
                                   Pageable pageable);

    @Query("SELECT MAX(c.customerCode) FROM Customer c WHERE c.customerCode LIKE 'KH%'")
    String findMaxCustomerCode();
}