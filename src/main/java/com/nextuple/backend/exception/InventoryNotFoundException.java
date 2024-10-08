package com.nextuple.backend.exception;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(String message){
        super(message);
    }
}
