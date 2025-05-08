package org.example.electricstore.exception.supplier;

import lombok.Getter;

@Getter
public enum SupplierError {
    INVALID_NAME_FORMAT("Tên nhà cung cấp chỉ được chứa chữ cái và khoảng trắng"),
    INVALID_PHONE_FORMAT("Số điện thoại phải có 10 số và bắt đầu bằng số 0"),
    INVALID_EMAIL_FORMAT("Email không hợp lệ hoặc đã tồn tại trong hệ thống"),
    INVALID_ADDRESS_FORMAT("Địa chỉ không được để trống và phải từ 5 đến 200 ký tự"),
    SUPPLIER_NOT_FOUND("Không tìm thấy nhà cung cấp"),
    DUPLICATE_SUPPLIER_CODE("Mã nhà cung cấp đã tồn tại"),
    DUPLICATE_PHONE("Số điện thoại đã tồn tại"),
    DUPLICATE_EMAIL("Email đã tồn tại");

    private final String message;

    SupplierError(String message) {
        this.message = message;
    }
}