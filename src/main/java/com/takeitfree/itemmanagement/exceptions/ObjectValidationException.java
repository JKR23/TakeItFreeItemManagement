package com.takeitfree.itemmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
public class ObjectValidationException extends RuntimeException {

    private Set<String> errorMessages;

    @Override
    public String getMessage() {
        return String.join("\n", errorMessages);
    }
}
