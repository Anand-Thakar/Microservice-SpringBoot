package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDtoRequest {

    private long buyingProductId;
    private long totalAmount;
    private long quantity;
    private PaymentMode paymentMode;
}
