package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductDtoRequest {

    private String name;
    private long price;
    private long quantity;
}
