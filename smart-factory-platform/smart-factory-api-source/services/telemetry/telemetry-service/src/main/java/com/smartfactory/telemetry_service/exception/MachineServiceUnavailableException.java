package com.smartfactory.telemetry_service.exception;

public class MachineServiceUnavailableException extends RuntimeException{

    public MachineServiceUnavailableException(String message){
        super(message);
    }

    public MachineServiceUnavailableException(String message,Throwable cause){
        super(message, cause);
    }
}
