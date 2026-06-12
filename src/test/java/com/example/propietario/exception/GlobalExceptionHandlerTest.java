package com.example.propietario.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Debería manejar RuntimeException y retornar HTTP 400")
    void handleRuntimeException_ShouldReturnBadRequest() {
        var ex = new RuntimeException("Error inesperado en el sistema");

        var response = exceptionHandler.handleRuntimeException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        var body = response.getBody();
        assertTrue(body.toString().contains("Error inesperado en el sistema"));
        assertTrue(body.toString().contains("400"));
    }

    @Test
    @DisplayName("Debería manejar MethodArgumentNotValidException y retornar mapa de errores")
    void handleValidationExceptions_ShouldReturnValidationErrors() {
        var ex = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("dto", "email", "Formato de email inválido");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        var response = exceptionHandler.handleValidationExceptions(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // CORREGIDO
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        var body = (Map<String, String>) response.getBody();
        assertEquals("Formato de email inválido", body.get("email"));
    }

    @Test
    @DisplayName("Debería manejar HttpMessageNotReadableException y retornar JSON error")
    void handleReadableExceptions_ShouldReturnJsonError() {
        var ex = mock(HttpMessageNotReadableException.class);

        var response = exceptionHandler.handleReadableExceptions(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        var body = (Map<String, String>) response.getBody();
        assertEquals("Valor de campo invalido o formato de JSON incorrecto", body.get("error"));
    }
}