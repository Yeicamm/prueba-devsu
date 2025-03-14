package com.devsu.hackerearth.backend.account.config;

import org.springframework.http.HttpStatus;

public class TransactionNotFound extends AccountExceptionBase{
    public TransactionNotFound(String message){
        super(Error.builder()
                .type(Error.Type.TRANSACTION_NOT_FOUND)
                .message(message)
                .build(), HttpStatus.NOT_FOUND);
    }
}
