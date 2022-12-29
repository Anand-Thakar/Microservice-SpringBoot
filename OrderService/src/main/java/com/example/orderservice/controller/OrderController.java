package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.external.client.ProductService;
import com.example.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Long> placeOrder(@RequestBody OrderDtoRequest orderDtoRequest){

        Long id = orderService.placeOrder(orderDtoRequest);
        log.info("order is successfully placed for id: {}",id);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }

}
