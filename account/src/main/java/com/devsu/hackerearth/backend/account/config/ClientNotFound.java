package com.devsu.hackerearth.backend.account.config;

import org.springframework.http.HttpStatus;

public class ClientNotFound extends AccountExceptionBase{
    public ClientNotFound(String message){
        super(Error.builder()
                .type(Error.Type.CLIENT_NOT_FOUND)
                .message(message)
                .build(), HttpStatus.NOT_FOUND);
    }
}
