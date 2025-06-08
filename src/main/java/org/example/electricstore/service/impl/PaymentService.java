package org.example.electricstore.service.impl;

import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.enums.PaymentMethod;
import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.Payment;
import org.example.electricstore.repository.IOrderRepository;
import org.example.electricstore.repository.IPaymentRepository;
import org.example.electricstore.service.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    @Transactional
    public Integer addPayment(Integer orderId, Integer paymentType) {
        Payment payment = new Payment();
        Order order = orderRepository.findByOrderID(orderId);

        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId);
        }

        PaymentStatus paymentStatus;
        PaymentMethod paymentMethod;

        // Chỉ sử dụng 2 phương thức thanh toán: tiền mặt và chuyển khoản
        if(paymentType == 1){
            paymentStatus = PaymentStatus.PENDING;
            paymentMethod = PaymentMethod.ONLINE_BANKING;
        } else {
            paymentStatus = PaymentStatus.COMPLETED;
            paymentMethod = PaymentMethod.CASH;

            order.setStatus(OrderStatus.COMPLETE);
            orderRepository.save(order);
        }

        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setCreateAt(java.time.LocalDateTime.now());

        payment.setAmount(order.getTotalPrice() != null ? order.getTotalPrice().intValue() : 0);

        return paymentRepository.save(payment).getPaymentID();
    }

    @Override
    @Transactional
    public void updatePayment(Integer paymentId, Integer amount) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán"));

        payment.setAmount(amount);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.COMPLETE);
            orderRepository.save(order);
        }
    }
    @Override
    public Payment getPaymentByOrderId(Integer orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }
}