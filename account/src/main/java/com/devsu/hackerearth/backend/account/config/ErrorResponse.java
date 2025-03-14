package com.devsu.hackerearth.backend.account.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Error error;

    public ErrorResponse(final Error.Type type, final String message) {
        this.error = new Error(type,message,null);
    }
}
