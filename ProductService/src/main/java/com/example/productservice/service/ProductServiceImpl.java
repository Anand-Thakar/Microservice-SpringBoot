package com.example.productservice.service;

import com.example.productservice.dto.ProductDtoRequest;
import com.example.productservice.dto.ProductDtoResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductServiceCustomException;
import com.example.productservice.repo.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductDtoRequest productDtoRequest) {
        log.info("Creating new Product with help of productDtoRequest");

        Product product
                = Product.builder()
                .productName(productDtoRequest.getName())
                .quantity(productDtoRequest.getQuantity())
                .price(productDtoRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product Created and saved inside database");

        return product.getProductId();
    }

    @Override
    public ProductDtoResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND", 404));

        ProductDtoResponse productDtoResponse
                = new ProductDtoResponse();

        //Name of properties should be same then only this thing going to work
        copyProperties(product, productDtoResponse);

        return productDtoResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce for Id: {} and Quantity {} inside productServiceImpl ", productId,quantity);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given Id not found", "PRODUCT_NOT_FOUND",404));

        if(product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product does not have sufficient Quantity in records in database", "INSUFFICIENT_QUANTITY",400);
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated Successfully");

    }
}
