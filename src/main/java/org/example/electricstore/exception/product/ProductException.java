package org.example.electricstore.exception.product;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {
    private final ProductError errorCode;

    public ProductException(ProductError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}