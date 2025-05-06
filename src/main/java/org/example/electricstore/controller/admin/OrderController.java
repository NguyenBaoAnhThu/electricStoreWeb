package org.example.electricstore.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.DTO.order.ProductOrderChoiceDTO;
import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.example.electricstore.service.common.PDFService;
import org.example.electricstore.service.impl.CustomerService;
import org.example.electricstore.service.impl.OrderService;
import org.example.electricstore.service.impl.ProductService;
import org.example.electricstore.service.interfaces.IPaymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Admin/order")
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private IPaymentService paymentService;

    // Endpoint để hiển thị danh sách đơn hàng
    @GetMapping("")
    public String orderList(
            @RequestParam(value = "orderCode", required = false) String orderCode,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Model model) {

        // Chuyển đổi giá trị status từ form sang giá trị enum phù hợp
        OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                // Xử lý trường hợp giá trị status không hợp lệ
                status = null;
            }
        }

        // Lấy danh sách đơn hàng với các điều kiện lọc
        Page<Order> orders = orderService.getAllOrders(orderCode, customerName, status, fromDate, toDate, page, size);

        // Thêm dòng này để kiểm tra nếu không có đơn hàng
        if (orders.isEmpty()) {
            model.addAttribute("message", "Không có đơn hàng nào phù hợp với dữ liệu.");
        }

        // Thêm danh sách trạng thái đơn hàng vào model
        model.addAttribute("orderStatuses", OrderStatus.values());

        // Thêm dữ liệu vào model
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/order/listOrder";
    }

    // Endpoint để thêm đơn hàng mới
    @GetMapping("/add")
    public ModelAndView addOrder() {
        ModelAndView modelAndView = new ModelAndView();
        OrderDTO orderDTO = new OrderDTO();
        modelAndView.addObject("orderDTO", orderDTO);
        CustomerDTO customerDTO = new CustomerDTO();
        modelAndView.addObject("customerDTO", customerDTO);

        // Thêm danh sách trạng thái đơn hàng vào model
        modelAndView.addObject("orderStatuses", OrderStatus.values());

        modelAndView.setViewName("admin/order/addOrder");
        return modelAndView;
    }

    // Endpoint để tạo đơn hàng
    // Endpoint để tạo đơn hàng
    @PostMapping("/create")
    public Object createOrder(@Valid @ModelAttribute("orderDTO") OrderDTO orderDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Kiểm tra danh sách sản phẩm
            if (orderDTO.getProductOrderDTOList() == null || orderDTO.getProductOrderDTOList().isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("productOrderDTOList", "Danh sách sản phẩm không được trống");
                return ResponseEntity.badRequest().body(errors);
            }

            // Lấy giá trị giảm giá từ form
            String discountAmountStr = request.getParameter("discountAmount");
            String discountPercentStr = request.getParameter("discountPercent");

            // Xử lý giảm giá theo VNĐ
            if (discountAmountStr != null && !discountAmountStr.isEmpty()) {
                // Loại bỏ dấu phân cách và chuyển đổi sang số
                discountAmountStr = discountAmountStr.replaceAll("[^\\d]", "");
                try {
                    Double discountAmount = Double.parseDouble(discountAmountStr);
                    orderDTO.setDiscountAmount(discountAmount);
                } catch (NumberFormatException e) {
                    orderDTO.setDiscountAmount(0.0);
                }
            }

            // Xử lý giảm giá theo %
            if (discountPercentStr != null && !discountPercentStr.isEmpty()) {
                try {
                    Double discountPercent = Double.parseDouble(discountPercentStr);
                    orderDTO.setDiscountPercent(discountPercent);
                } catch (NumberFormatException e) {
                    orderDTO.setDiscountPercent(0.0);
                }
            }

            Integer customerId = (orderDTO.getCustomerDTO().getCustomerId() == null) ?
                    customerService.addCustomerAndGetId(orderDTO.getCustomerDTO()) :
                    orderDTO.getCustomerDTO().getCustomerId();

            orderDTO.setCustomerId(customerId);

            // Lưu đơn hàng và tạo thanh toán
            Integer orderId = orderService.saveOrder(orderDTO);
            System.out.println("Order ID: " + orderId);

            try {
                // Tạo thanh toán
                Integer paymentId = paymentService.addPayment(orderId, orderDTO.getPaymentMethod());
                System.out.println("Payment ID: " + paymentId);

                // Trả về kết quả với các thông tin cần thiết
                return ResponseEntity.ok().body("{\"orderId\": " + orderId +
                        ", \"isPrintInvoice\": " + orderDTO.getIsPrintInvoice() +
                        ", \"paymentId\": " + paymentId +
                        ", \"paymentMethod\": " + orderDTO.getPaymentMethod() + "}");
            } catch (Exception e) {
                // Nếu có lỗi khi tạo thanh toán, ghi lại lỗi nhưng vẫn trả về thông tin đơn hàng
                System.err.println("Lỗi khi tạo thanh toán: " + e.getMessage());
                e.printStackTrace();

                return ResponseEntity.ok().body("{\"orderId\": " + orderId +
                        ", \"isPrintInvoice\": " + orderDTO.getIsPrintInvoice() +
                        ", \"error\": \"Đơn hàng đã được tạo nhưng có lỗi khi xử lý thanh toán\"" +
                        ", \"paymentMethod\": " + orderDTO.getPaymentMethod() + "}");
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ tổng quát
            System.err.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Endpoint để tải xuống hóa đơn PDF
    @GetMapping("/downloadInvoicePdf")
    public void downloadInvoicePdf(@RequestParam Integer orderId, HttpServletResponse response) throws IOException {
        OrderDTO orderDTO = orderService.getOrderDTOById(orderId);

        byte[] pdf = pdfService.createInvoicePDF(orderDTO);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    // Endpoint để hiển thị danh sách khách hàng
    @GetMapping("/showListCustomer")
    public String listCustomers(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "filter", required = false, defaultValue = "name") String filter,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {

        Page<Customer> customers = (keyword != null && !keyword.isEmpty())
                ? customerService.searchCustomers(keyword, filter, page, size)
                : customerService.getAllCustomers(page, size);

        model.addAttribute("customerDTO", customers);
        model.addAttribute("customers", customers);
        assert keyword != null;
        model.addAttribute("keyword", keyword);
        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/order/OldCustomer";
    }

    // Endpoint để hiển thị danh sách sản phẩm
    @GetMapping("/showListProduct")
    public String listProducts(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "oldData", required = false) String listIdAndQuantity,
            Model model) {

        Page<ProductOrderChoiceDTO> products = productService.getProducts(keyword, page, size);
        model.addAttribute("products", products.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("oldData", listIdAndQuantity);

        return "admin/order/OldProduct";
    }

    @GetMapping("/next-code")
    @ResponseBody
    public String getNextOrderCode() {
        return orderService.getNextOrderCode();
    }
    @ModelAttribute("statusMap")
    public Map<String, String> statusMap() {
        Map<String, String> map = new HashMap<>();
        map.put("COMPLETE", "ĐÃ HOÀN THÀNH");
        map.put("CANCELLED", "ĐÃ HỦY");
        map.put("DELIVERED", "ĐÃ GIAO HÀNG");
        return map;
    }
    // Endpoint để xem chi tiết đơn hàng
    @GetMapping("/view/{id}")
    public String viewOrderDetail(@PathVariable("id") Integer orderId, Model model) {
        try {
            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                return "redirect:/Admin/order?error=OrderNotFound";
            }

            // Lấy thông tin thanh toán (nếu có)
            var payment = paymentService.getPaymentByOrderId(orderId);

            // Thêm dữ liệu vào model
            model.addAttribute("order", order);
            model.addAttribute("payment", payment);

            // Đảm bảo statusMap được thêm vào
            model.addAttribute("statusMap", statusMap());

            return "admin/order/orderDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/Admin/order?error=ViewError";
        }
    }
    // Thêm endpoint này vào class OrderController của bạn
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") Integer orderId) {
        try {
            // Gọi service để hủy đơn hàng
            boolean result = orderService.cancelOrder(orderId);

            if (result) {
                return ResponseEntity.ok("Đơn hàng đã được hủy thành công");
            } else {
                return ResponseEntity.badRequest().body("Không thể hủy đơn hàng này");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi hủy đơn hàng: " + e.getMessage());
        }
    }
    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(HttpServletRequest request) {
        try {
            // Lấy đơn hàng từ session
            OrderDTO orderDTO = (OrderDTO) request.getSession().getAttribute("pendingOrder");

            if (orderDTO == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin đơn hàng");
            }

            // Lưu đơn hàng vào DB
            Integer orderId = orderService.saveOrder(orderDTO);

            // Tạo thanh toán
            Integer paymentId = paymentService.addPayment(orderId, orderDTO.getPaymentMethod());

            // Xóa đơn hàng khỏi session
            request.getSession().removeAttribute("pendingOrder");

            // Trả về kết quả thành công
            return ResponseEntity.ok("{\"success\": true, \"orderId\": " + orderId + "}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xác nhận thanh toán: " + e.getMessage());
        }
    }

    // Thêm endpoint để hủy thanh toán
    @PostMapping("/cancel-payment")
    public ResponseEntity<String> cancelPayment(HttpServletRequest request) {
        try {
            // Xóa đơn hàng khỏi session
            request.getSession().removeAttribute("pendingOrder");

            // Trả về kết quả thành công
            return ResponseEntity.ok("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi hủy thanh toán: " + e.getMessage());
        }
    }
}