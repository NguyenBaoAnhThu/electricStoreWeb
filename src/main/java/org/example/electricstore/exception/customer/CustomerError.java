package org.example.electricstore.exception.customer;


import lombok.Getter;

@Getter
public enum CustomerError {
    CUSTOMER_NOTFOUND("Khách hàng không tồn tại.") ,
    ID_NOTFOUND("ID khách hàng không tồn tại.") ,
    INVALID_PHONE_NUMBER("Số điện thoại đã tồn tại.") ,
    INVALID_EMAIL("Email không hợp lệ.") ,
    EMAIL_EXISTS("Email đã tồn tại.");
    private final String message;
    CustomerError(String message) {
        this.message = message;
    }
}
