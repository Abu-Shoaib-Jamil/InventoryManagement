package com.nextuple.backend.exception;

public class InvalidInventoryQuantityException extends RuntimeException{
    public InvalidInventoryQuantityException(String message){
        super(message);
    }
}
