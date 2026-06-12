package com.retail.rewards.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private ResourceNotFoundException resourceNotFoundException;

    @Mock
    private InvalidTransactionException invalidTransactionException;

    @Mock
    private CustomerDataNotFoundException customerDataNotFoundException;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleNotFoundException(){

        ResponseEntity<String> response = exceptionHandler.handleNotFoundException(resourceNotFoundException);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testHandleInvalidTransaction(){
        ResponseEntity<String> response = exceptionHandler.handleInvalidTransaction(invalidTransactionException);
        assertEquals(400,response.getStatusCodeValue());
    }

    @Test
    void testHandleCustomerDataNotFound(){
        ResponseEntity<String> response = exceptionHandler.handleCustomerDataNotFound(customerDataNotFoundException);
        assertEquals(204,response.getStatusCode().value());
    }


    @Test
    void testHandleGenericException() {

        Exception ex = new Exception("Unexpected error");

        ResponseEntity<String> response =
                exceptionHandler.handleGenricException(ex);

        assertEquals(500, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Unexpected error"));
    }

}
