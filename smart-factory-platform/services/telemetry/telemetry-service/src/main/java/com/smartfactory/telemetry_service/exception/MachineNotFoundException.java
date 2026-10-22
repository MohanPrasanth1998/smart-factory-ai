package com.smartfactory.telemetry_service.exception;

public class MachineNotFoundException extends RuntimeException{

    public MachineNotFoundException(String message){
        super(message);
    }
}
