package com.takeitfree.itemmanagement.exceptions;

public class UserNotConnectedException extends RuntimeException {
    public UserNotConnectedException(String message) {
        super(message);
    }
}
