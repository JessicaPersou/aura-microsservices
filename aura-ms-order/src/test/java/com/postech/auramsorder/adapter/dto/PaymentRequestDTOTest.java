package com.postech.auramsorder.adapter.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        Long orderId = 123L;
        String cardNumber = "4111111111111111";
        BigDecimal amount = BigDecimal.valueOf(250.75);
        Long clientId = 456L;

        // Act
        paymentRequest.setOrderId(orderId);
        paymentRequest.setCardNumber(cardNumber);
        paymentRequest.setAmount(amount);
        paymentRequest.setClientId(clientId);

        // Assert
        assertEquals(orderId, paymentRequest.getOrderId());
        assertEquals(cardNumber, paymentRequest.getCardNumber());
        assertEquals(amount, paymentRequest.getAmount());
        assertEquals(clientId, paymentRequest.getClientId());
    }

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long orderId = 123L;
        String cardNumber = "4111111111111111";
        BigDecimal amount = BigDecimal.valueOf(250.75);
        Long clientId = 456L;

        // Act
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(orderId, cardNumber, amount, clientId);

        // Assert
        assertEquals(orderId, paymentRequest.getOrderId());
        assertEquals(cardNumber, paymentRequest.getCardNumber());
        assertEquals(amount, paymentRequest.getAmount());
        assertEquals(clientId, paymentRequest.getClientId());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();

        // Assert
        assertNotNull(paymentRequest);
    }
}