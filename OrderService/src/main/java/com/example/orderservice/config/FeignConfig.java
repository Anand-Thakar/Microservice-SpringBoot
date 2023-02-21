package com.example.orderservice.config;

import com.example.orderservice.external.decoder.ExceptionMessage;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder errorDecoder (){
        return new ExceptionMessage();
    }
}
