package com.smartfactory.machine.exception;

public class MachineAlreadyExistsException extends  RuntimeException{

    public MachineAlreadyExistsException(String message){
        super(message);
    }
}
