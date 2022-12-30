package com.example.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallback(){
        return "Order Service is down, Please try after some time";
    }
    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack(){
        return "Payment Service is down, Please try after some time";
    }
    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack(){
        return "Product Service is down, Please try after some time";
    }

}
