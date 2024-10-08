package com.nextuple.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException productNotFoundException){
        return new ResponseEntity<>(productNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleProductExistException(ProductExistException productExistException){
        return new ResponseEntity<>(productExistException.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInventoryNotFoundException(InventoryNotFoundException inventoryNotFoundException){
        return new ResponseEntity<>(inventoryNotFoundException.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidInventoryQuantityException(InvalidInventoryQuantityException invalidInventoryQuantityException){
        return new ResponseEntity<>(invalidInventoryQuantityException.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
