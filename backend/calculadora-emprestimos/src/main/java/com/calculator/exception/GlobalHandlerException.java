package com.calculator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

    static final Logger logger = LoggerFactory.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(DateException.class)
    public ResponseEntity<String> handleDateException(DateException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundInstallments.class)
    public ResponseEntity<String> handleNotFoundInstallments(NotFoundInstallments e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        logger.error("Erro 500 inesperado: {}", e.getMessage(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor. Tente novamente mais tarde");
    }

}
