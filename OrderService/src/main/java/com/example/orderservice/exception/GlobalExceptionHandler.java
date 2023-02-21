package com.example.orderservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(exception.getMessage())
                .errorCodeName(exception.getErrorCodeName())
                .build(),
                HttpStatus.valueOf(exception.getStatus()));
    }



    @ExceptionHandler(Exception.class)
    public  ResponseEntity<ErrorResponse> handleOtherException(Exception ex){
        return  new ResponseEntity<>(new ErrorResponse().builder()
                .errorCodeName("Something wrong with order system")
                .errorMessage(ex.getMessage())
                .errorCodeName("500")
                .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
