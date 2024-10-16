package com.nextuple.backend.exception;

public class InvalidOrderTypeException extends RuntimeException{
    public InvalidOrderTypeException(String message){
        super(message);
    }

}
