package com.retail.rewards.exception;

import com.retail.rewards.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

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
     * Handles cases where a transaction is invalid (e.g., negative amount).
     *
     * @param ex thrown when transaction data is invalid
     * @return an error response and HTTP 400 (BAD_REQUEST) status
     */

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransaction(InvalidTransactionException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where customer data is missing or unavailable.
     *
     * @param ex thrown when no customer data is found
     * @return an error response and HTTP 404 (NOT_FOUND) status
     */
    @ExceptionHandler(CustomerDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerDataNotFound(CustomerDataNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles cases where page number is out of bound.
     *
     * @param ex thrown when page number is out of bound
     * @return an error response and HTTP Bad request status
     */
    @ExceptionHandler(PageNumberOutOfBoundException.class)
    public ResponseEntity<ErrorResponse> handlePaginationError(PageNumberOutOfBoundException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exception where we pass invalid inputs as requestParams or path variables.
     *
     * @param ex thrown when invalid inputs
     * @return an error response and HTTP Bad request status
     */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles any generic or unexpected exceptions not explicitly mapped.
     *
     * @param ex thrown during application execution
     * @return a generic error response and HTTP 500 (INTERNAL_SERVER_ERROR) status
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenricException(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "something went wrong: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
