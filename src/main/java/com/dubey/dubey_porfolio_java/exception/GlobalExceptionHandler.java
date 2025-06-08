package com.dubey.dubey_porfolio_java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Global Exception Handler
@ControllerAdvice
public class GlobalExceptionHandler {

     // Handling a specific exception
     @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
         return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
     }
 
     // Handling a generic exception
     @ExceptionHandler(Exception.class)
     public ResponseEntity<String> handleGlobalException(Exception ex) {
         return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
     }

}
