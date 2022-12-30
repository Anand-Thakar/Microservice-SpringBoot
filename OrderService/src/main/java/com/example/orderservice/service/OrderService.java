package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;

public interface OrderService {
    Long placeOrder(OrderDtoRequest orderDtoRequest);

    OrderDtoResponse getOrderDetails(long orderId);

}
