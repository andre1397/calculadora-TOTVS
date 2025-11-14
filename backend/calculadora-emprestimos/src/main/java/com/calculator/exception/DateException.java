package com.calculator.exception;

public class DateException extends RuntimeException{

    public DateException(String message) {
        super("Erro: " + message);
    }
}
