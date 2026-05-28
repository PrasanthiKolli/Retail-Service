package com.retail.rewards.exception;

/**
 * Exception thrown when a requested resource not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
