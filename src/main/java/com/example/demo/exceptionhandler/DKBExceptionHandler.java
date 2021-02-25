package com.example.demo.exceptionhandler;

import com.example.demo.error.ApiError;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.NotEnoughBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class  DKBExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccountLockedException.class)
    public ResponseEntity<Object> handleAccountLockedException(AccountLockedException ex){
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),errors);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex){
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),errors);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotEnoughBalanceException.class)
    public ResponseEntity<Object> handleNotEnoughBalanceException(NotEnoughBalanceException ex){
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),errors);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
