package com.retail.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the Retail Rewards application.
 * <p>
 * This class uses Spring's {@link ControllerAdvice} to intercept exceptions thrown
 * across the application and return appropriate HTTP responses with meaningful messages.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a requested resource is not found.
     *
     * @param ex  thrown when a resource cannot be located
     * @return an exception message and HTTP 404 (NOT_FOUND) status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where a transaction is invalid (e.g., negative amount).
     *
     * @param ex  thrown when transaction data is invalid
     * @return an exception message and HTTP 400 (BAD_REQUEST) status
     */

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<String> handleInvalidTransaction(InvalidTransactionException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles cases where customer data is missing or unavailable.
     *
     * @param ex  thrown when no customer data is found
     * @return an exception message and HTTP 204 (NO_CONTENT) status
     */
    @ExceptionHandler(CustomerDataNotFoundException.class)
    public ResponseEntity<String> handleCustomerDataNotFound(CustomerDataNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }

    /**
     * Handles cases where page number is out of bound.
     *
     * @param ex  thrown when page number is out of bound
     * @return an exception message and HTTP Bad request status
     */
    @ExceptionHandler(PageNumberOutOfBoundException.class)
    public ResponseEntity<String> handlePaginationError(PageNumberOutOfBoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any generic or unexpected exceptions not explicitly mapped.
     *
     * @param ex thrown during application execution
     * @return a generic error message and HTTP 500 (INTERNAL_SERVER_ERROR) status
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenricException(Exception ex){
        return new ResponseEntity<>("something went wrong: "+ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
