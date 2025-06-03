package org.example.electricstore.controller;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPIController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/select/{keyword}")
    public List<CustomerDTO> getCustomersByKeyword(@PathVariable String keyword) {
        return this.customerService.getCustomersByKeyword(keyword);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExists(@RequestParam String phone) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", customerService.existsByPhoneNumber(phone));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", customerService.existsByEmail(email));
        return ResponseEntity.ok(response);
    }
}
