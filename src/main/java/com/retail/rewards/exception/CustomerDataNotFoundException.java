package com.retail.rewards.exception;

/**
 * Exception thrown when Customer data is empty.
 *
 */
public class CustomerDataNotFoundException extends RuntimeException {

    public CustomerDataNotFoundException(String message) {
        super(message);
    }
}
