package com.bilvantis.exception;

public class ApplicationException extends RuntimeException{
    public ApplicationException(String msg){
        super(msg);
    }
    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApplicationException(Throwable throwable) {
        super(throwable);
    }

}