package com.retail.rewards.exception;

/**
 * Exception thrown when page number is out of bound.
 */
public class PageNumberOutOfBoundException extends RuntimeException {
    public PageNumberOutOfBoundException(String message) {
        super(message);
    }
}
