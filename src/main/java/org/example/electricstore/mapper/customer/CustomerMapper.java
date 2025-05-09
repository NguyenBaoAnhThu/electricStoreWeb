package org.example.electricstore.mapper.customer;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.model.Customer;
import org.example.electricstore.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    @Autowired
    private ICustomerRepository customerRepository;

    public Customer convertToCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .customerName(customerDTO.getCustomerName())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .birthDate(customerDTO.getBirthDate())
                .email(customerDTO.getEmail())
                .build();

        // ĐÃ SỬA: Chỉ tạo mã mới nếu DTO không có mã
        if (customerDTO.getCustomerCode() != null && !customerDTO.getCustomerCode().isEmpty()) {
            // Sử dụng mã khách hàng từ DTO nếu có
            customer.setCustomerCode(customerDTO.getCustomerCode());
        } else {
            // Tự động tạo mã khách hàng mới nếu chưa có
            customer.setCustomerCode(generateCustomerCode());
        }

        return customer;
    }

    public CustomerDTO convertToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .customerCode(customer.getCustomerCode())  // Thêm customerCode
                .customerName(customer.getCustomerName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .birthDate(customer.getBirthDate())
                .email(customer.getEmail())
                .build();
    }

    // Phương thức sinh mã khách hàng tự động
    public String generateCustomerCode() {
        // Tìm mã khách hàng lớn nhất hiện tại
        String maxCode = customerRepository.findMaxCustomerCode();

        int nextSequence = 1;
        if (maxCode != null && maxCode.startsWith("KH")) {
            try {
                String numberPart = maxCode.substring(2);
                nextSequence = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // Nếu có lỗi, bắt đầu lại từ 1
                nextSequence = 1;
            }
        }
        return String.format("KH%04d", nextSequence);
    }
}