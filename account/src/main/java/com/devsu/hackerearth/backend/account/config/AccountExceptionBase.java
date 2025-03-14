package com.devsu.hackerearth.backend.account.config;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AccountExceptionBase extends RuntimeException{
    private final Error error;
    private final HttpStatus httpStatus;
    private final Error.Type type;

    protected AccountExceptionBase(Error error, HttpStatus httpStatus){
        this.error = error;
        this.type = getType();
        this.httpStatus = httpStatus;
    }
}
