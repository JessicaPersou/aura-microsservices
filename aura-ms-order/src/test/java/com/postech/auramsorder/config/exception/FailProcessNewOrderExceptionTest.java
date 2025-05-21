package com.postech.auramsorder.config.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FailProcessNewOrderExceptionTest {

    @Test
    void testFailProcessNewOrderExceptionMessage() {
        String errorMessage = "Failed to process new order";
        String errorDetails = "Order ID is invalid";

        FailProcessNewOrderException exception = assertThrows(FailProcessNewOrderException.class, () -> {
            throw new FailProcessNewOrderException(errorMessage, errorDetails);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}