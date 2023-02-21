package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.dto.PaymentMode;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.external.client.PaymentService;
import com.example.orderservice.external.client.ProductService;
import com.example.orderservice.external.dto.PaymentDtoRequest;
import com.example.orderservice.external.dto.PaymentDtoResponse;
import com.example.orderservice.external.dto.ProductDtoResponse;
import com.example.orderservice.repo.OrderRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @Test
    public void testWhenOrderSuccessForGetOrderDetails(){

        //mocking
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getBuyingProductId(),
                ProductDtoResponse.class))
                .thenReturn( mockProductDtoResponse());

        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentDtoResponse.class))
                .thenReturn(mockPaymentDtoResponse());
        //actual
        OrderDtoResponse orderDtoResponse =orderService.getOrderDetails(1);

        //verification
        verify(orderRepository,times(1)).findById(anyLong());
        verify(restTemplate,times(1)).getForObject("http://PRODUCT-SERVICE/product/" + order.getBuyingProductId(),
                ProductDtoResponse.class);
        verify(restTemplate,times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentDtoResponse.class);

        //assert
        assertNotNull(orderDtoResponse);
        assertEquals(order.getId(),orderDtoResponse.getOrderId());

    }

    @Test
    public void whenGetOrderNotFoundFailedScenario(){
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        CustomException exception =
                assertThrows(CustomException.class, () -> orderService.getOrderDetails(1));

        assertEquals(404,exception.getStatus());
        assertEquals("NOT_FOUND",exception.getErrorCodeName());

        verify(orderRepository,times(1)).findById(anyLong());
    }



    private PaymentDtoRequest mockPaymentDtorequest(Order order) {

        return PaymentDtoRequest.builder()
                .orderId(order.getId())
                .amount(200l)
                .paymentMode(PaymentMode.CASH)
                .referenceNumber("aths26518")
                .build();
    }

    private OrderDtoRequest mockOrderDtoRequest() {

        return  OrderDtoRequest.builder()
                .buyingProductId(23l)
                .paymentMode(PaymentMode.CASH)
                .quantity(20l)
                .totalAmount(200l)
                .build();
    }

    private PaymentDtoResponse mockPaymentDtoResponse() {

        return PaymentDtoResponse.builder()
                .orderId(1l)
                .paymentId(20l)
                .paymentMode(PaymentMode.CASH)
                .paymentDate(Instant.now())
                .amount(200l)
                .build();
    }

    private ProductDtoResponse mockProductDtoResponse() {

        return ProductDtoResponse.builder()
                .productId(23L)
                .productName("Laptop")
                .price(200L)
                .quantity(10L)
                .build();
    }


    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("SUCCESS")
                .amount(200L)
                .quantity(10L)
                .buyingProductId(23L)
                .orderDate(Instant.now())
                .id(1L)
                .build();
    }

}