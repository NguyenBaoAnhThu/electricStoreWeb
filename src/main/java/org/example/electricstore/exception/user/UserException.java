package org.example.electricstore.exception.user;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserError errorCode;

    public UserException(UserError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public UserError getErrorCode() {
        return this.errorCode;
    }
}