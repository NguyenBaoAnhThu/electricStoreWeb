package org.example.electricstore.exception.user;

import lombok.Getter;

@Getter
public enum UserError {
    INVALID_PHONE_FORMAT("Số điện thoại phải đủ 10 số, không hơn không kém"),
    INVALID_AGE("Tuổi phải lớn hơn 15"),
    INVALID_NAME_FORMAT("Tên chỉ được chứa chữ, số và khoảng trắng"),
    INVALID_USERNAME_LENGTH("Tên đăng nhập không được để trống"),
    INVALID_EMAIL_FORMAT("Email không hợp lệ hoặc đã tồn tại trong hệ thống");

    private final String message;

    UserError(String message) {
        this.message = message;
    }
}