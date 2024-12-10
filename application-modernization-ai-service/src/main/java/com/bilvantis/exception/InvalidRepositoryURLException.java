package com.bilvantis.exception;

public class InvalidRepositoryURLException extends RuntimeException{
    public InvalidRepositoryURLException(String msg){
        super(msg);
    }
    public InvalidRepositoryURLException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InvalidRepositoryURLException(Throwable throwable) {
        super(throwable);
    }

}