package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;

public interface OrderService {
    Long placeOrder(OrderDtoRequest orderDtoRequest);
}
