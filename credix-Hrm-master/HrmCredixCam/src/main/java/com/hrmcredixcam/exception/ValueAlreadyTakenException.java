package com.hrmcredixcam.exception;

public class ValueAlreadyTakenException extends Exception{

    public ValueAlreadyTakenException(String entityName,String value) {
        super(entityName+" with value <<"+value+ ">> already taken, use another....");
    }
}
