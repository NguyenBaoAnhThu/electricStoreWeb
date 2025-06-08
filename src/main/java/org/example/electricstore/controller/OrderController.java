package org.example.electricstore.controller;

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

    @GetMapping("")
    public String orderList(
            @RequestParam(value = "searchField", required = false) String searchField,
            @RequestParam(value = "searchInput", required = false) String searchInput,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "msg", required = false) String msg,
            @RequestParam(value = "success", required = false) String success,
            Model model, RedirectAttributes redirectAttributes) {

        if (success != null && success.equals("added")) {
            return "redirect:/Admin/order?successMessage=Thêm đơn hàng mới thành công.";
        }

        String orderCode = null;
        String customerName = null;
        String phoneNumber = null;

        if (searchField != null && searchInput != null && !searchInput.isEmpty()) {
            switch (searchField) {
                case "orderCode":
                    orderCode = searchInput;
                    break;
                case "customerName":
                    customerName = searchInput;
                    break;
                case "phoneNumber":
                    phoneNumber = searchInput;
                    break;
            }
        }

        Page<Order> orders = orderService.getAllOrders(orderCode, customerName, phoneNumber, null, null, page, size);

        if (orders.isEmpty() && (searchInput != null && !searchInput.isEmpty())) {
            model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm." +
                    "\n");
        }

        if (msg != null) {
            if ("cancel_ok".equals(msg)) {
                model.addAttribute("successMessage", "Hủy đơn hàng thành công.");
            } else if ("cancel_fail".equals(msg)) {
                model.addAttribute("errorMessage", "Không thể hủy đơn hàng này");
            } else if ("cancel_error".equals(msg)) {
                model.addAttribute("errorMessage", "Đã xảy ra lỗi khi hủy đơn hàng");
            }
        }

        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/order/listOrder";
    }

    @GetMapping("/add")
    public ModelAndView addOrder() {
        ModelAndView modelAndView = new ModelAndView();
        OrderDTO orderDTO = new OrderDTO();
        modelAndView.addObject("orderDTO", orderDTO);
        CustomerDTO customerDTO = new CustomerDTO();
        modelAndView.addObject("customerDTO", customerDTO);
        modelAndView.addObject("orderStatuses", OrderStatus.values());

        modelAndView.setViewName("admin/order/addOrder");
        return modelAndView;
    }

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
            if (orderDTO.getProductOrderDTOList() == null || orderDTO.getProductOrderDTOList().isEmpty()) {
                Map<String, String> errors = new HashMap<>();
                errors.put("productOrderDTOList", "Danh sách sản phẩm không được trống");
                return ResponseEntity.badRequest().body(errors);
            }

            String discountAmountStr = request.getParameter("discountAmount");
            String discountPercentStr = request.getParameter("discountPercent");

            // Xử lý giảm giá theo VNĐ
            if (discountAmountStr != null && !discountAmountStr.isEmpty()) {
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

            Integer orderId = orderService.saveOrder(orderDTO);
            System.out.println("Order ID: " + orderId);

            try {
                Integer paymentId = paymentService.addPayment(orderId, orderDTO.getPaymentMethod());
                System.out.println("Payment ID: " + paymentId);

                return ResponseEntity.ok().body("{\"orderId\": " + orderId +
                        ", \"isPrintInvoice\": " + orderDTO.getIsPrintInvoice() +
                        ", \"paymentId\": " + paymentId +
                        ", \"paymentMethod\": " + orderDTO.getPaymentMethod() +
                        ", \"success\": true}");
            } catch (Exception e) {
                System.err.println("Lỗi khi tạo thanh toán: " + e.getMessage());
                e.printStackTrace();

                return ResponseEntity.ok().body("{\"orderId\": " + orderId +
                        ", \"isPrintInvoice\": " + orderDTO.getIsPrintInvoice() +
                        ", \"error\": \"Đơn hàng đã được tạo nhưng có lỗi khi xử lý thanh toán\"" +
                        ", \"paymentMethod\": " + orderDTO.getPaymentMethod() +
                        ", \"success\": true}");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

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

    @GetMapping("/showListCustomer")
    public String listCustomers(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "filter", required = false, defaultValue = "customerName") String filter,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Model model) {

        Page<Customer> customers;

        if (keyword != null && !keyword.isEmpty()) {
            customers = customerService.searchCustomers(keyword, filter, page, size);
        } else {
            customers = customerService.getAllCustomers(page, size);
        }

        if (customers.isEmpty() && keyword != null && !keyword.isEmpty()) {
            model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
        }

        model.addAttribute("customers", customers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("filter", filter);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/order/OldCustomer";
    }

    @GetMapping("/showListProduct")
    public String listProducts(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "oldData", required = false) String listIdAndQuantity,
            Model model) {

        Page<ProductOrderChoiceDTO> products = productService.getProducts(keyword, page, size);
        if (products.isEmpty() && keyword != null && !keyword.isEmpty()) {
            model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
        }

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
        return map;
    }

    @GetMapping("/view/{id}")
    public String viewOrderDetail(@PathVariable("id") Integer orderId, Model model) {
        try {
            Order order = orderService.getOrderById(orderId);

            if (order == null) {
                return "redirect:/Admin/order?errorMessage=Không tìm thấy đơn hàng";
            }

            var payment = paymentService.getPaymentByOrderId(orderId);

            model.addAttribute("order", order);
            model.addAttribute("payment", payment);
            model.addAttribute("statusMap", statusMap());

            return "admin/order/orderDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/Admin/order?errorMessage=Đã xảy ra lỗi khi xem chi tiết đơn hàng";
        }
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrderWithRedirect(@PathVariable("id") Integer orderId) {
        try {
            boolean result = orderService.cancelOrder(orderId);

            if (result) {
                return "redirect:/Admin/order?msg=cancel_ok";
            } else {
                return "redirect:/Admin/order?msg=cancel_fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/Admin/order?msg=cancel_error";
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(HttpServletRequest request) {
        try {
            OrderDTO orderDTO = (OrderDTO) request.getSession().getAttribute("pendingOrder");

            if (orderDTO == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin đơn hàng");
            }

            Integer orderId = orderService.saveOrder(orderDTO);

            Integer paymentId = paymentService.addPayment(orderId, orderDTO.getPaymentMethod());

            request.getSession().removeAttribute("pendingOrder");

            return ResponseEntity.ok("{\"success\": true, \"orderId\": " + orderId + "}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xác nhận thanh toán: " + e.getMessage());
        }
    }

    @PostMapping("/cancel-payment")
    public ResponseEntity<String> cancelPayment(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute("pendingOrder");
            return ResponseEntity.ok("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi hủy thanh toán: " + e.getMessage());
        }
    }
}