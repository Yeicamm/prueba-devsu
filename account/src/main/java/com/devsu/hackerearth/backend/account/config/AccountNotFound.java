package com.devsu.hackerearth.backend.account.config;

import org.springframework.http.HttpStatus;

public class AccountNotFound extends AccountExceptionBase {
    public AccountNotFound(String message){
        super(Error.builder()
                .type(Error.Type.ACCOUNT_NOT_FOUND)
                .message(message)
                .build(), HttpStatus.NOT_FOUND);
    }
}
