package com.smartfactory.machine.exception;

public class FactoryNotFoundException extends RuntimeException{

    public FactoryNotFoundException(String message){
        super(message);
    }
}
