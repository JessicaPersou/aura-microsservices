package com.postech.auramsorder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderConstructorAndGetters() {
        Long id = 1L;
        Long clientId = 2L;
        String items = "Item1, Item2";
        LocalDateTime dtCreate = LocalDateTime.now();
        String status = "ABERTO";
        BigDecimal totalAmount = new BigDecimal("100.00");
        Long paymentId = 3L;
        String paymentCardNumber = "1234567890123456";

        Order order = new Order(id, clientId, items, dtCreate, status, totalAmount, paymentId, paymentCardNumber);

        assertEquals(id, order.getId());
        assertEquals(clientId, order.getClientId());
        assertEquals(items, order.getItems());
        assertEquals(dtCreate, order.getDtCreate());
        assertEquals(status, order.getStatus());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(paymentId, order.getPaymentId());
        assertEquals(paymentCardNumber, order.getPaymentCardNumber());
    }

    @Test
    void testSetters() {
        Order order = new Order();

        order.setId(1L);
        order.setClientId(2L);
        order.setItems("Item1, Item2");
        order.setDtCreate(LocalDateTime.now());
        order.setStatus("ABERTO");
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setPaymentId(3L);
        order.setPaymentCardNumber("1234567890123456");

        assertEquals(1L, order.getId());
        assertEquals(2L, order.getClientId());
        assertEquals("Item1, Item2", order.getItems());
        assertNotNull(order.getDtCreate());
        assertEquals("ABERTO", order.getStatus());
        assertEquals(new BigDecimal("100.00"), order.getTotalAmount());
        assertEquals(3L, order.getPaymentId());
        assertEquals("1234567890123456", order.getPaymentCardNumber());
    }

    @Test
    void testToString() {
        Order order = new Order(1L, 2L, "Item1, Item2", LocalDateTime.now(), "ABERTO", new BigDecimal("100.00"), 3L, "1234567890123456");
        String toString = order.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("clientId=2"));
        assertTrue(toString.contains("items='Item1, Item2'"));
        assertTrue(toString.contains("status='ABERTO'"));
        assertTrue(toString.contains("totalAmount=100.00"));
        assertTrue(toString.contains("paymentId=3"));
        assertTrue(toString.contains("paymentCardNumber='1234567890123456'"));
    }
}