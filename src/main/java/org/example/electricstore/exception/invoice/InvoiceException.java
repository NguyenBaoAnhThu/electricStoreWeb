package org.example.electricstore.exception.invoice;

import lombok.Getter;

@Getter
public class InvoiceException extends RuntimeException {
    private final InvoiceError errorCode;

    public InvoiceException(InvoiceError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}