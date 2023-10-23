package com.hrmcredixcam.exception;

public class DoesNotExistException extends Exception{
    public DoesNotExistException(String entity,String value) {
        super(entity+" : <<"+value+">> does not exist");
    }


}
