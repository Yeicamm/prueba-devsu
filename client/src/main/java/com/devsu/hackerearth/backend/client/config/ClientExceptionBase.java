package com.devsu.hackerearth.backend.client.config;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class ClientExceptionBase extends RuntimeException{
    private final Error error;
    private final HttpStatus httpStatus;
    private final Error.Type type;

    protected ClientExceptionBase(Error error, HttpStatus httpStatus){
        this.error = error;
        this.type = getType();
        this.httpStatus = httpStatus;
    }
}
