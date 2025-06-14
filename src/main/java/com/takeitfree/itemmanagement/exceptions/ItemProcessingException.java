package com.takeitfree.itemmanagement.exceptions;

public class ItemProcessingException extends RuntimeException {
    public ItemProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
