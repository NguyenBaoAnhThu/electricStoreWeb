package org.example.electricstore.DTO.customer;

import org.example.electricstore.vatidator.customer.DobConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Integer customerId;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải có độ dài từ 2 đến 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String customerName ;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$",
            message = "Số điện thoại phải bắt đầu bằng +84 hoặc 0, theo sau là 3,5,7,8,9 và 8 chữ số")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 2, max = 200, message = "Địa chỉ phải có độ dài từ 5 đến 200 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9À-ỹ,\\s.-]+$",
            message = "Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu chấm, dấu phẩy và dấu gạch ngang")
    private String address;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được để trống !")
    @DobConstraint(min = 15 , message = "Không đủ điều kiện , tuổi phải lớn hơn 15 !")
    private LocalDate birthDate;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ, vui lòng nhập đúng định dạng")
    @Size(min = 5, max = 100, message = "Email phải có độ dài từ 5 đến 100 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email phải theo định dạng chuẩn (ví dụ: example@domain.com)")
    private String email;

//    public CustomerDTO(Customer customer) {
//    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                '}';
    }
}
