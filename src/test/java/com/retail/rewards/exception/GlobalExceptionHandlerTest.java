package com.retail.rewards.exception;

import com.retail.rewards.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
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

    @Mock
    private PageNumberOutOfBoundException pageNumberOutOfBoundException;

    @Mock
    private ConstraintViolationException constraintViolationException;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleNotFoundException(){

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFoundException(resourceNotFoundException);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testHandleInvalidTransaction(){
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidTransaction(invalidTransactionException);
        assertEquals(400,response.getStatusCode().value());
    }

    @Test
    void testHandleCustomerDataNotFound(){
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCustomerDataNotFound(customerDataNotFoundException);
        assertEquals(404,response.getStatusCode().value());
    }

    @Test
    void testHandlePaginationError(){
        ResponseEntity<ErrorResponse> response = exceptionHandler.handlePaginationError(pageNumberOutOfBoundException);
        assertEquals(400,response.getStatusCode().value());
    }

    @Test
    void testHandleConstraintViolationError(){
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals(400,response.getStatusCode().value());
    }


    @Test
    void testHandleGenericException() {

        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleGenricException(ex);

        assertEquals(500, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Unexpected error"));
    }

}
