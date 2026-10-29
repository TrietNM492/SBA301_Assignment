package com.edu.fpt.exception;

import com.edu.fpt.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        int status = 404;
        String message = e.getMessage();
        String error = "Resource Not Found!";
        ApiResponse<Object> response = new ApiResponse<>(status, message, null, error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {InvalidFieldInputException.class})
    public ResponseEntity<ApiResponse<Object>> handlebarValidationException(InvalidFieldInputException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        Map<String, String> details = new LinkedHashMap<String, String>();
        for (var error : e.getFieldErrors()) {
            var key = error.getField();
            var value = error.getDefaultMessage();
            details.put(key, value);
        }

        String message = details.toString();
        String error = "Validation Failed";
        ApiResponse<Object> response = new ApiResponse<>(status, message, null, error);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(value = {EmailAlreadyExistException.class})
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExistException(EmailAlreadyExistException e) {
        int status = HttpStatus.CONFLICT.value();
        String message = e.getMessage();
        String error = "RESOURCE_CONFLICT";
        ApiResponse<Object> response = new ApiResponse<>(status, message, null, error);
        return ResponseEntity.status(status).body(response);
    }
}
