package com.hrmcredixcam.exception;

public class AlreadyExistException extends Exception{
    public AlreadyExistException(String entity, String value) {
        super(entity+" : <<"+value+">> already exist");
    }
}
