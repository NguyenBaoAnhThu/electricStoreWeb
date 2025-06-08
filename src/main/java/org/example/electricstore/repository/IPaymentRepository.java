package org.example.electricstore.repository;

import org.example.electricstore.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT p FROM Payment p WHERE p.order.orderID = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Integer orderId);
}