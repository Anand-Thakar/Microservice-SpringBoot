package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.external.client.PaymentService;
import com.example.orderservice.external.client.ProductService;
import com.example.orderservice.external.dto.PaymentDtoRequest;
import com.example.orderservice.external.dto.PaymentDtoResponse;
import com.example.orderservice.external.dto.ProductDtoResponse;
import com.example.orderservice.repo.OrderRepository;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
@NoArgsConstructor
public class OrderServiceImpl implements OrderService{
    private OrderRepository orderRepository;
    private ProductService productService;
    private PaymentService paymentService;
    private RestTemplate restTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, PaymentService paymentService, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Long placeOrder(OrderDtoRequest orderDtoRequest) {

        //Rest Api call using feign client
        //before doing this we want to check with help of product service that we have required quantity
        //available or not to serve the request.
        productService.reduceQuantity(orderDtoRequest.getBuyingProductId(),orderDtoRequest.getQuantity());

        log.info("Placing Order orderDtoRequest in orderServiceImpl: {}", orderDtoRequest);


        //Order Entity -> Save the data with Status Order Created
        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderDtoRequest.getTotalAmount())
                .orderStatus("CREATED")
                .buyingProductId(orderDtoRequest.getBuyingProductId())
                .orderDate(Instant.now())
                .quantity(orderDtoRequest.getQuantity())
                .build();

        order = orderRepository.save(order);


        //Payment Service -> Payments -> Success-> COMPLETE, Else
        log.info("Calling Payment Service to complete the payment");
        PaymentDtoRequest paymentDtoRequest
                = PaymentDtoRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderDtoRequest.getPaymentMode())
                .amount(orderDtoRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentDtoRequest);
            log.info("Payment done Successfully. Changing the Order status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order Places successfully with Order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderDtoResponse getOrderDetails(long orderId) {

        log.info("Get order details for Order Id : {}", orderId);
        Order order;
        order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                        "NOT_FOUND",
                        404));


        log.info(order);
        log.info("In order to get all details for the order," +
                "Invoking Product service to fetch the product for id: {}", order.getBuyingProductId());
        ProductDtoResponse productDtoResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getBuyingProductId(),
                ProductDtoResponse.class);

        OrderDtoResponse.ProductDetails productDetails
                = OrderDtoResponse.ProductDetails
                .builder()
                .productName(productDtoResponse.getProductName())
                .productId(productDtoResponse.getProductId())
                .price(productDtoResponse.getQuantity())
                .quantity(productDtoResponse.getQuantity())
                .build();

        log.info("Getting payment information form the payment Service");
       PaymentDtoResponse paymentDtoResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
               PaymentDtoResponse.class);

        OrderDtoResponse.PaymentDetails paymentDetails = OrderDtoResponse.PaymentDetails.builder()
                .paymentId(paymentDtoResponse.getPaymentId())
                .paymentMode(paymentDtoResponse.getPaymentMode())
                .paymentStatus(paymentDtoResponse.getStatus())
                .paymentDate(paymentDtoResponse.getPaymentDate())
                .build();

        OrderDtoResponse orderDtoResponse
                = OrderDtoResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderDtoResponse;
    }
}
