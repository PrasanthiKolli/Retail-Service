package com.retail.rewards.exception;

/**
 * Exception thrown when transaction amount is negative.
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
