package com.bilvantis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.bilvantis.util.AppModernizationAPIConstants.GLOBAL_FIELD_ID;


@ControllerAdvice
public class AppModernizationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ProjectImplementationSaveFailedException.class)
    public ResponseEntity<ErrorResponse> handleProjectImplementationSaveFailedException(ProjectImplementationSaveFailedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRepositoryURLException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRepositoryURLException(InvalidRepositoryURLException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), GLOBAL_FIELD_ID);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}