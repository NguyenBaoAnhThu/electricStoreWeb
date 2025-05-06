package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Payment;

public interface IPaymentService {
    Integer addPayment(Integer orderId, Integer paymentMethod);
    void updatePayment(Integer paymentId, Integer amount);
    Payment getPaymentByOrderId(Integer orderId);
}