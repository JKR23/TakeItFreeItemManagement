package com.takeitfree.itemmanagement.globalExceptionHandler;

import com.takeitfree.itemmanagement.exceptions.ObjectValidationException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RepresentationException> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(
                        RepresentationException.builder()
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<RepresentationException> handleEntityExistsException(EntityExistsException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(
                        RepresentationException.builder()
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ObjectValidationException.class)
    public ResponseEntity<RepresentationException> handleObjectValidationException(ObjectValidationException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(
                        RepresentationException.builder()
                                .errorMessage(e.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RepresentationException> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(
                        RepresentationException.builder()
                                .errorMessage("check your argument, it doesn't match")
                                .build()
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RepresentationException> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(
                        RepresentationException.builder()
                                .errorMessage(e.getMessage()+"\n"+e.getCause().toString()+"\n"+ Arrays.toString(e.getStackTrace()))
                                .build()
                );
    }
}
