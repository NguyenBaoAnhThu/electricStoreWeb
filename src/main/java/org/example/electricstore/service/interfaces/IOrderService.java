package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.DTO.order.OrderDetailDTO;
import org.example.electricstore.DTO.order.OrderHistoryRq;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    Integer saveOrder(OrderDTO orderDTO);
    Order getOrderById(Integer id);
    List<OrderHistoryRq> getAllOrderHistoryRqByCustomer(Customer customer);
    List<OrderDetailDTO> getAllOrderDetailDTOByCustomer(int orderId);
    Page<CustomerDTO> getAllCustomersDTO(Integer page, Integer size);
    List<Order> getCompletedOrders(LocalDateTime startDate, LocalDateTime endDate);
    long getTotalCompletedOrders(LocalDateTime startDate, LocalDateTime endDate);
    double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);
    OrderDTO getOrderDTOById(Integer orderId);
    Page<Order> getAllOrders(String orderCode, String customerName, String status,
                             String fromDate, String toDate, int page, int size);
}