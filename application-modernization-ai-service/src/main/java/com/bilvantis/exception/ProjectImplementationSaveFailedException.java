package com.bilvantis.exception;

public class ProjectImplementationSaveFailedException extends RuntimeException {
    public ProjectImplementationSaveFailedException(String mes) {
        super(mes);
    }

    public ProjectImplementationSaveFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}