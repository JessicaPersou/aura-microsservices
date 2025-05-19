package com.postech.auramspayment.adapter.controller.dto;

import com.postech.auramspayment.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDTOTest {

    @Test
    void testDefaultConstructor() {
        PaymentDTO paymentDTO = new PaymentDTO();
        assertNull(paymentDTO.getId());
        assertNull(paymentDTO.getOrderId());
        assertNull(paymentDTO.getCardNumber());
        assertNull(paymentDTO.getAmount());
        assertNull(paymentDTO.getStatus());
        assertNull(paymentDTO.getCreatedAt());
        assertNull(paymentDTO.getUpdatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDateTime now = LocalDateTime.now();
        PaymentDTO paymentDTO = new PaymentDTO(
                1L,
                "12345",
                "1234567890123456",
                new BigDecimal("100.00"),
                PaymentStatus.COMPLETED,
                now,
                now
        );

        assertEquals(1L, paymentDTO.getId());
        assertEquals("12345", paymentDTO.getOrderId());
        assertEquals("1234567890123456", paymentDTO.getCardNumber());
        assertEquals(new BigDecimal("100.00"), paymentDTO.getAmount());
        assertEquals(PaymentStatus.COMPLETED, paymentDTO.getStatus());
        assertEquals(now, paymentDTO.getCreatedAt());
        assertEquals(now, paymentDTO.getUpdatedAt());
    }

    @Test
    void testDefaultValuesInConstructor() {
        PaymentDTO paymentDTO = new PaymentDTO(
                1L,
                "12345",
                "1234567890123456",
                new BigDecimal("100.00"),
                null,
                null,
                null
        );

        assertEquals(PaymentStatus.PENDING, paymentDTO.getStatus());
        assertNotNull(paymentDTO.getCreatedAt());
        assertNotNull(paymentDTO.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        PaymentDTO paymentDTO = new PaymentDTO();
        LocalDateTime now = LocalDateTime.now();

        paymentDTO.setId(1L);
        paymentDTO.setOrderId("12345");
        paymentDTO.setCardNumber("1234567890123456");
        paymentDTO.setAmount(new BigDecimal("100.00"));
        paymentDTO.setStatus(PaymentStatus.COMPLETED);
        paymentDTO.setCreatedAt(now);
        paymentDTO.setUpdatedAt(now);

        assertEquals(1L, paymentDTO.getId());
        assertEquals("12345", paymentDTO.getOrderId());
        assertEquals("1234567890123456", paymentDTO.getCardNumber());
        assertEquals(new BigDecimal("100.00"), paymentDTO.getAmount());
        assertEquals(PaymentStatus.COMPLETED, paymentDTO.getStatus());
        assertEquals(now, paymentDTO.getCreatedAt());
        assertEquals(now, paymentDTO.getUpdatedAt());
    }
}