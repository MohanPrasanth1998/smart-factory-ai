package com.smartfactory.telemetry_service.exception;

public class TelemetryNotFoundException extends RuntimeException{

    public TelemetryNotFoundException(String message){
        super(message);
    }
}
