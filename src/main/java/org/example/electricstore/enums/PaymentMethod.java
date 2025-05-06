package org.example.electricstore.enums;

public enum PaymentMethod {
    CASH(1, "Tiền mặt"),
    ONLINE_BANKING(2, "Chuyển khoản");

    private final int code;
    private final String description;

    PaymentMethod(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromCode(int code) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.code == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy phương thức thanh toán với mã: " + code);
    }
}