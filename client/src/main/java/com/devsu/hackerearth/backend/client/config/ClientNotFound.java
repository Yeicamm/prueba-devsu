package com.devsu.hackerearth.backend.client.config;

import org.springframework.http.HttpStatus;

public class ClientNotFound extends ClientExceptionBase{
    public ClientNotFound(String message){
        super(Error.builder()
                .type(Error.Type.CLIENT_NOT_FOUND)
                .message(message)
                .build(), HttpStatus.NOT_FOUND);
    }
}
