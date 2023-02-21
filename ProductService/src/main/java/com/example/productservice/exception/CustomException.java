package com.example.productservice.exception;

import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException{

    private String errorCodeName;
    private int status;
    public ProductServiceCustomException(String message, String errorCodeName, int status) {
        super(message);
        this.errorCodeName = errorCodeName;
        this.status = status;
    }
}
