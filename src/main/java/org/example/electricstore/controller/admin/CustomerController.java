package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.OrderDetailDTO;
import org.example.electricstore.DTO.order.OrderHistoryRq;
import org.example.electricstore.model.Customer;
import org.example.electricstore.service.impl.CustomerService;
import org.example.electricstore.service.impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Controller
@RequestMapping("/Admin/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;

    private final List<String> FIELD_PRIORITY = Arrays.asList(
            "customerName", "phoneNumber", "address", "birthDate", "email"
    );

    private final Map<String, List<String>> ERROR_PRIORITY = new HashMap<>();

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;

        ERROR_PRIORITY.put("customerName", Arrays.asList("NotBlank", "Size", "Pattern"));
        ERROR_PRIORITY.put("phoneNumber", Arrays.asList("NotBlank", "Pattern"));
        ERROR_PRIORITY.put("address", Arrays.asList("NotBlank", "Size", "Pattern"));
        ERROR_PRIORITY.put("birthDate", Arrays.asList("NotNull", "DobConstraint"));
        ERROR_PRIORITY.put("email", Arrays.asList("NotBlank", "Size", "Email", "Pattern"));
    }

    @GetMapping
    public ModelAndView getAndSearchCustomers(@RequestParam(name = "searchField", required = false) String field,
                                              @RequestParam(name = "searchInput", required = false, defaultValue = "") String keyword,
                                              @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/listCustomer");
        Page<Customer> customerPage;
        String filterKeyWord = keyword.trim();
        if (!filterKeyWord.isEmpty()) {
            customerPage = this.customerService.searchByFieldAndKey(field, filterKeyWord, page, size);
            if (customerPage.isEmpty()) {
                modelAndView.addObject("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
            }
        } else {
            customerPage = this.customerService.getAllCustomers(page, size);
        }
        modelAndView.addObject("customers", customerPage);
        modelAndView.addObject("field", field);
        modelAndView.addObject("filterKeyWord", filterKeyWord);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", customerPage.getTotalPages());
        return modelAndView;
    }

    @GetMapping("/history/{id}")
    public ModelAndView getCustomerHistory(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/historyCustomer");
        Customer customer = this.customerService.getCustomerById(id);
        List<OrderHistoryRq> orderHistoryRqs = this.orderService.getAllOrderHistoryRqByCustomer(customer);
        modelAndView.addObject("orderHistoryRqs", orderHistoryRqs);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @GetMapping("/api/orders/{orderId}/details")
    @ResponseBody
    public List<OrderDetailDTO> getOrdersDetails(@PathVariable("orderId") Integer orderId) {
        return this.orderService.getAllOrderDetailDTOByCustomer(orderId);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            for (String fieldName : FIELD_PRIORITY) {
                if (bindingResult.hasFieldErrors(fieldName)) {
                    processFieldErrorsByPriority(bindingResult, errors, fieldName);
                }
            }
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!FIELD_PRIORITY.contains(error.getField()) && !errors.containsKey(error.getField())) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            this.customerService.updateCustomer(customerDTO, customerDTO.getCustomerId());
            return ResponseEntity.ok("Đã cập nhật khách hàng thành công!");
        } catch (Exception e) {
            Map<String, String> errors = new HashMap<>();
            if (e.getMessage().contains("INVALID_PHONE_NUMBER")) {
                errors.put("phoneNumber", "Số điện thoại đã được sử dụng bởi khách hàng khác");
            } else if (e.getMessage().contains("EMAIL_EXISTS")) {
                errors.put("email", "Email đã được sử dụng bởi khách hàng khác");
            } else if (e.getMessage().contains("INVALID_EMAIL")) {
                errors.put("email", "Email không hợp lệ, vui lòng nhập đúng định dạng");
            } else if (e.getMessage().contains("CUSTOMER_NOTFOUND")) {
                errors.put("customerId", "Không tìm thấy khách hàng");
            } else {
                errors.put("general", "Có lỗi xảy ra: " + e.getMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
    }

    private void processFieldErrorsByPriority(BindingResult bindingResult, Map<String, String> errors, String fieldName) {
        if (errors.containsKey(fieldName)) {
            return;
        }
        List<FieldError> fieldErrors = bindingResult.getFieldErrors(fieldName);
        List<String> errorCodePriority = ERROR_PRIORITY.getOrDefault(fieldName, List.of());
        for (String errorCode : errorCodePriority) {
            for (FieldError error : fieldErrors) {
                if (error.getCode() != null && error.getCode().contains(errorCode)) {
                    errors.put(fieldName, error.getDefaultMessage());
                    return;
                }
            }
        }
        if (!fieldErrors.isEmpty() && !errors.containsKey(fieldName)) {
            errors.put(fieldName, fieldErrors.get(0).getDefaultMessage());
        }
    }

    @GetMapping("/next-code")
    @ResponseBody
    public String getNextCustomerCode() {
        return customerService.getNextCustomerCode();
    }
}