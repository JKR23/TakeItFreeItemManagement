package com.takeitfree.itemmanagement.globalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RepresentationException {
    private String errorMessage;
}
