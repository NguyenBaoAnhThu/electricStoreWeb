package org.example.electricstore.exception.invoice;

import lombok.Getter;

@Getter
public enum InvoiceError {
    FUTURE_IMPORT_DATE("Ngày nhập không thể ở tương lai"),
    SUPPLIER_REQUIRED("Vui lòng chọn nhà cung cấp");

    private final String message;

    InvoiceError(String message) {
        this.message = message;
    }
}