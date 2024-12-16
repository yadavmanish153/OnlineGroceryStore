package com.nuvei.ecomm.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(MissingOrderNumberException.class)
    public ResponseEntity<String> handleMissingOrderNumber(MissingOrderNumberException ex){
        return new ResponseEntity<>(ex.getErrorMessage().toString(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemExpiredException.class)
    public ResponseEntity<String> handleItemExpired(ItemExpiredException ex){
        return new ResponseEntity<>(ex.getErrorMessage().toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex){
        return new ResponseEntity<>(ex.getErrorMessage().toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage().toString(), HttpStatus.BAD_REQUEST);
    }

}
