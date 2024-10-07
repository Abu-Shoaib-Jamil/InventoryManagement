package com.nextuple.backend.exception;


public class ProductExistException extends RuntimeException{
    public ProductExistException(String message){
        super(message);
    }

}
