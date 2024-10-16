package com.nextuple.backend.exception;

public class InvalidInventoryOperationException extends RuntimeException{
    public InvalidInventoryOperationException(String message){
        super(message);
    }
}
