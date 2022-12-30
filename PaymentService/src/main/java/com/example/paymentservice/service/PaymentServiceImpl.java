package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDtoRequest;
import com.example.paymentservice.dto.PaymentDtoResponse;
import com.example.paymentservice.dto.PaymentMode;
import com.example.paymentservice.entity.TransactionDetails;
import com.example.paymentservice.repo.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    private TransactionDetailsRepository transactionDetailsRepo;

    public PaymentServiceImpl(TransactionDetailsRepository transactionDetailsRepo) {
        this.transactionDetailsRepo = transactionDetailsRepo;
    }

    @Override
    public Long doPayment(PaymentDtoRequest paymentDtoRequest) {
        log.info("Recording Payment Details: {}", paymentDtoRequest);

        TransactionDetails transactionDetails
                = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentDtoRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentDtoRequest.getOrderId())
                .referenceNumber(paymentDtoRequest.getReferenceNumber())
                .amount(paymentDtoRequest.getAmount())
                .build();

        transactionDetailsRepo.save(transactionDetails);

        log.info("Transaction Completed with Id: {}", transactionDetails.getId());

        return transactionDetails.getId();
    }

    @Override
    public PaymentDtoResponse getPaymentDetailsByOrderId(String orderId) {
       TransactionDetails details = transactionDetailsRepo.findByOrderId(Long.valueOf(orderId));
        PaymentDtoResponse paymentResponse
                = PaymentDtoResponse.builder()
                .paymentId(details.getId())
                .paymentMode(PaymentMode.valueOf(details.getPaymentMode()))
                .paymentDate(details.getPaymentDate())
                .orderId(details.getOrderId())
                .status(details.getPaymentStatus())
                .amount(details.getAmount())
                .build();

        return  paymentResponse;
    }
}
