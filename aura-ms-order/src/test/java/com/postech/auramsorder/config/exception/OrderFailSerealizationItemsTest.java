package com.postech.auramsorder.config.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderFailSerealizationItemsTest {

    @Test
    void testOrderFailSerealizationItemsMessage() {
        String errorMessage = "Failed to serialize order items";
        String errorDetails = "Item list is null";

        OrderFailSerealizationItems exception = assertThrows(OrderFailSerealizationItems.class, () -> {
            throw new OrderFailSerealizationItems(errorMessage, errorDetails);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}