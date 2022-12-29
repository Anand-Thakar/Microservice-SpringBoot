package com.example.productservice.service;

import com.example.productservice.dto.ProductDtoRequest;
import com.example.productservice.dto.ProductDtoResponse;

public interface ProductService {
    long addProduct(ProductDtoRequest productDtoRequest);

    ProductDtoResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);

}
