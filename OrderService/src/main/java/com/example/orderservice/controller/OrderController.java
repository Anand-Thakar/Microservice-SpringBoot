package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<Long> placeOrder(@RequestBody OrderDtoRequest orderDtoRequest){

        Long id = orderService.placeOrder(orderDtoRequest);
        log.info("order is successfully placed for id: {}",id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDtoResponse> getOrderDetails(@PathVariable("orderId") long orderId) {
        OrderDtoResponse orderResponse
                = orderService.getOrderDetails(orderId);

        return new ResponseEntity<>(orderResponse,
                HttpStatus.OK);
    }

}
