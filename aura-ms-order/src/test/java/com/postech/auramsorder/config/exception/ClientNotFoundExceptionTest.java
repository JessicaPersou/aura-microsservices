package com.postech.auramsorder.config.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientNotFoundExceptionTest {

    @Test
    void testClientNotFoundExceptionMessage() {
        String errorMessage = "Client not found";
        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            throw new ClientNotFoundException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }
}