package com.example.orderservice.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException{
    private String errorCodeName;
    private int status;

    public CustomException(String message, String errorCodeName, int status) {
        super(message);
        this.errorCodeName = errorCodeName;
        this.status = status;
    }
}
