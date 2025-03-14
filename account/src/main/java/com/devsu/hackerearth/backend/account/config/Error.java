package com.devsu.hackerearth.backend.account.config;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Error implements Serializable {

    private Type type;
    private String message;
    private List<String> detail;

    public enum Type {
        ACCOUNT_NOT_FOUND,
        TRANSACTION_NOT_FOUND,
        CLIENT_NOT_FOUND
    }
}
