package org.example.electricstore.repository;

import org.example.electricstore.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    // Cách 1: Sử dụng JPQL query
    @Query("SELECT p FROM Payment p WHERE p.order.orderID = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Integer orderId);

    // HOẶC Cách 2: Sử dụng quy ước đặt tên của Spring Data
    // Optional<Payment> findByOrder_OrderID(Integer orderId);
}