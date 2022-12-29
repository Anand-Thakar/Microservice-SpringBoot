package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;

//    private ProductDetails productDetails;
//    private PaymentDetails paymentDetails;


}
