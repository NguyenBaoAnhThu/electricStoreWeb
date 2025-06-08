package org.example.electricstore.repository;

import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    List<Order> findByStatusAndCreateAtBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByCustomer(Customer customer);
    Order findByOrderID(Integer orderId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE order_products SET total_price = ?2 WHERE order_id = ?1", nativeQuery = true)
    void updateTotalPrice(Integer orderId, Double totalPrice);
    Order findTopByOrderByOrderIDDesc();
}
