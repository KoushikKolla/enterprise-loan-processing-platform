package com.elpp.common.exception;

import com.elpp.common.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiResponse<Void> response=new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(response);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex){
        ApiResponse<Void> response= new ApiResponse<>(
                false,
                ex.getMessage(),
                null);
        return ResponseEntity.
                badRequest().
                body(response);
    }
@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex){
        String message= ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation failed");
        ApiResponse<Void> response= new ApiResponse<>(
                false,
                message,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
}
@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourcesException(DuplicateResourceException ex){
        ApiResponse<Void> response= new ApiResponse<>(false,ex.getMessage(),null);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
}
@ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequestException(InvalidRequestException ex){
        ApiResponse<Void> response= new ApiResponse<>(false,ex.getMessage(),null);
        return ResponseEntity
                .badRequest()
                .body(response);
}

}
