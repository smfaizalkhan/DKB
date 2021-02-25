package com.example.demo.exceptionhandler;

import com.example.demo.error.ApiError;
import com.example.demo.exception.AccountLockedException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.NotEnoughBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DKBExceptionHandlerTest {

    @InjectMocks
    private DKBExceptionHandler dkbExceptionHandler;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error = new FieldError("TransactionDTO","toAccount","Needed");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(methodArgumentNotValidException.getBindingResult().getFieldErrors()).thenReturn(Collections.singletonList(error));
        ResponseEntity<Object> responseEntity = dkbExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);
        assertTrue(responseEntity.getBody() instanceof ApiError);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleAccountLockedException() {
        AccountLockedException accountLockedException =
                new AccountLockedException("Account Locked for Testing Account"+ UUID.randomUUID());
        ResponseEntity<Object> responseEntity = dkbExceptionHandler.handleAccountLockedException(accountLockedException);
        assertTrue(responseEntity.getBody() instanceof ApiError);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertEquals(accountLockedException.getMessage(),apiError.getMessage());
    }

    @Test
    void handleAccountNotFoundException() {
        AccountNotFoundException accountNotFoundException =
                new AccountNotFoundException("AccountNotFoundException for Testing Account"+ UUID.randomUUID());
        ResponseEntity<Object> responseEntity = dkbExceptionHandler.handleAccountNotFoundException(accountNotFoundException);
        assertTrue(responseEntity.getBody() instanceof ApiError);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertEquals(accountNotFoundException.getMessage(),apiError.getMessage());
    }

    @Test
    void handleNotEnoughBalanceException() {
        NotEnoughBalanceException notEnoughBalanceException =
                new NotEnoughBalanceException("NotEnoughBalanceException for Testing Account"+ UUID.randomUUID());
        ResponseEntity<Object> responseEntity = dkbExceptionHandler.handleNotEnoughBalanceException(notEnoughBalanceException);
        assertTrue(responseEntity.getBody() instanceof ApiError);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ApiError apiError = (ApiError) responseEntity.getBody();
        assertEquals(notEnoughBalanceException.getMessage(),apiError.getMessage());
    }
}