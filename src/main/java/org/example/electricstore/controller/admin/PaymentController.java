package org.example.electricstore.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.Payment;
import org.example.electricstore.repository.IOrderRepository;
import org.example.electricstore.repository.IPaymentRepository;
import org.example.electricstore.service.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/Admin/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @GetMapping("/qr-code")
    public String showQrCodePage(@RequestParam Integer orderId, Model model) {
        // Lấy thông tin đơn hàng
        Order order = orderRepository.findByOrderID(orderId);
        if (order == null) {
            return "redirect:/Admin/order?error=Order+not+found";
        }

        // Lấy thông tin thanh toán
        Payment payment = order.getPayment();
        if (payment == null) {
            return "redirect:/Admin/order?error=Payment+not+found";
        }

        // Thêm thông tin vào model
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);

        // Thêm orderId và paymentId riêng biệt vào model
        // để sử dụng trong thuộc tính data
        model.addAttribute("orderId", order.getOrderID());
        model.addAttribute("paymentId", payment.getPaymentID());

        return "admin/payment/qr-code";
    }

    @PostMapping("/confirm-payment")
    @ResponseBody
    public ResponseEntity<?> confirmPayment(@RequestParam Integer orderId, @RequestParam Integer paymentId) {
        try {
            // Lấy thông tin đơn hàng
            Order order = orderRepository.findByOrderID(orderId);
            if (order == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy đơn hàng");
            }

            // Cập nhật trạng thái đơn hàng
            order.setStatus(OrderStatus.COMPLETE);
            orderRepository.save(order);

            // Cập nhật trạng thái thanh toán
            paymentService.updatePayment(paymentId, order.getTotalPrice().intValue());

            return ResponseEntity.ok("Thanh toán thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xác nhận thanh toán: " + e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/cancel-payment")
    @ResponseBody
    public ResponseEntity<?> cancelPayment(@RequestParam Integer orderId) {
        try {
            // Xóa hoàn toàn đơn hàng và thanh toán liên quan từ database
            Order order = orderRepository.findByOrderID(orderId);
            if (order != null) {
                // Xóa trực tiếp đơn hàng (cascade sẽ xóa luôn payment và orderDetail)
                orderRepository.delete(order);
                return ResponseEntity.ok("Đã xóa đơn hàng thành công");
            } else {
                return ResponseEntity.ok("Đơn hàng không tồn tại hoặc đã bị xóa");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
    }
}