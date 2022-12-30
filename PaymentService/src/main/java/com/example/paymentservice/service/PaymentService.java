package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDtoRequest;
import com.example.paymentservice.dto.PaymentDtoResponse;

public interface PaymentService {
    Long doPayment(PaymentDtoRequest paymentDtoRequest);

    PaymentDtoResponse getPaymentDetailsByOrderId(String orderId);
}

