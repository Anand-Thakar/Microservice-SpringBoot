package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.external.client.ProductService;
import com.example.orderservice.repo.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{


    private OrderRepository orderRepository;


    private ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public Long placeOrder(OrderDtoRequest orderDtoRequest) {
        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED


        //rest api call using feign client
        productService.reduceQuantity(orderDtoRequest.getBuyingProductId(),orderDtoRequest.getQuantity());


        //before doing this we want to check with help of product service that we have required quantity
        //available or not to serve the request.

        log.info("Placing Order oderDtoRequest in orderServiceImpl: {}", orderDtoRequest);


        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderDtoRequest.getTotalAmount())
                .orderStatus("CREATED")
                .buyingProductId(orderDtoRequest.getBuyingProductId())
                .orderDate(Instant.now())
                .quantity(orderDtoRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        return order.getId();
    }
}
