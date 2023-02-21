package com.example.orderservice.external.decoder;
import com.example.orderservice.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class ExceptionMessage implements ErrorDecoder {


    @Override
    public Exception decode(final String s, final Response response) {

        log.info("::{}",  response.request().url());
        log.info("::{}",  response.request().headers());
        ErrorDecoder errorDecoder = new Default();

        ExceptionMessage message = null;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        switch (response.status()) {
            case 400:
                return new CustomException("INSUFFICNT QUANTITY of product","400",400);
            case 404:
                return new CustomException("Product with given id not found", "PRODUCT_NOT_FOUND", 404);
            default:
                return errorDecoder.decode(s, response);
        }

    }
}
