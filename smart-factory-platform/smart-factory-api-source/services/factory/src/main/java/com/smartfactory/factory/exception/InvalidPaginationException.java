package com.smartfactory.factory.exception;

import jakarta.persistence.criteria.CriteriaBuilder;

public class InvalidPaginationException extends RuntimeException{

    public InvalidPaginationException(String message){
        super(message);
    }
}
