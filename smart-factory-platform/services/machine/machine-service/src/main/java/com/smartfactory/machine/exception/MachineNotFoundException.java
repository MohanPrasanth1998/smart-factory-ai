package com.smartfactory.machine.exception;

public class MachineNotFoundException extends RuntimeException{

    public MachineNotFoundException(String message){
        super(message);
    }
}
