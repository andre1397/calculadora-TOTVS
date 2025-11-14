package com.calculator.exception;

public class NotFoundInstallments extends RuntimeException {

    public NotFoundInstallments(String message) {
        super(message);
    }
}
