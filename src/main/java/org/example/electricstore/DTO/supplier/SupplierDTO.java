package org.example.electricstore.DTO.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupplierDTO {

    private Integer id;

    private String supplierCode;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Size(min = 2, max = 100, message = "Tên nhà cung cấp phải có độ dài từ 2 đến 100 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "Tên nhà cung cấp chỉ được chứa chữ cái và khoảng trắng")
    private String supplierName;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 200, message = "Địa chỉ phải có độ dài từ 5 đến 200 ký tự")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0[0-9]{9}$",
            message = "Số điện thoại phải có 10 số và bắt đầu bằng số 0")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ, vui lòng nhập đúng định dạng")
    @Size(min = 5, max = 100, message = "Email phải có độ dài từ 5 đến 100 ký tự")
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}