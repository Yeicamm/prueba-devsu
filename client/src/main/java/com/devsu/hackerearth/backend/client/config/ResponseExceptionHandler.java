package com.devsu.hackerearth.backend.client.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { ClientNotFound.class })
    public ResponseEntity<ErrorResponse> handlerClientNotFoundEx(ClientNotFound exClientNotFound) {
        return new ResponseEntity<>(
                new ErrorResponse(exClientNotFound.getError().getType(), exClientNotFound.getError().getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
