package org.example.electricstore.exception.supplier;

import lombok.Getter;

@Getter
public class SupplierException extends RuntimeException {
    private final SupplierError errorCode;

    public SupplierException(SupplierError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SupplierException(SupplierError errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}