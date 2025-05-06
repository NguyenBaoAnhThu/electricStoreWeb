package org.example.electricstore.DTO.user;

import jakarta.persistence.Column;
import org.example.electricstore.vatidator.customer.DobConstraint;
import org.example.electricstore.vatidator.customer.UniqueEmail;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;

    @NotBlank(message = "Tên đăng nhập không được để trống. ")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống. ")
    private String password;

    @NotBlank(message = "Email không được để trống. ")
    @Email(message = "Email không hợp lệ, vui lòng nhập đúng định dạng")
    @Size(min = 5, max = 100, message = "Email phải có độ dài từ 5 đến 100 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email phải theo định dạng chuẩn (ví dụ: example@domain.com)")
    @UniqueEmail
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;

    @NotBlank(message = "Tên nhân viên không được để trống. ")
    @Size(min = 2, max = 50, message = "Tên phải có độ dài từ 2 đến 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String employeeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được để trống. ")
    @DobConstraint(min = 15, message = "Không đủ điều kiện, tuổi phải lớn hơn 15.")
    private LocalDate employeeBirthday;

    @NotBlank(message = "Địa chỉ không được để trống. ")
    @Size(min = 5, max = 200, message = "Địa chỉ phải có độ dài từ 5 đến 200 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9À-ỹ,\\s.-]+$",
            message = "Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu chấm, dấu phẩy và dấu gạch ngang")
    private String employeeAddress;

    @NotBlank(message = "Số điện thoại không được để trống. ")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$",
            message = "Số điện thoại phải bắt đầu bằng +84 hoặc 0, theo sau là 3,5,7,8,9 và 8 chữ số")
    private String employeePhone;

    @NotBlank(message = "Công việc không được để trống. ")
    @Size(max = 100, message = "Công việc không được vượt quá 100 ký tự")

    // Lưu trữ vai trò người dùng
    private String role; // "ROLE_ADMIN" hoặc "ROLE_EMPLOYEE"

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", employeeCode='" + employeeCode + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeBirthday=" + employeeBirthday +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", employeePhone='" + employeePhone + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}