package com.postech.auramspayment.gateway.external;

import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockPaymentGatewayAdapterTest {

    private MockPaymentGatewayAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MockPaymentGatewayAdapter();
    }

    @Test
    void testProcessPayment_NullPayment() {
        PaymentStatus status = adapter.processPayment(null);
        assertEquals(PaymentStatus.FAILED, status);
    }

    @Test
    void testProcessPayment_CardNumberStartsWith4() {
        Payment payment = new Payment();
        payment.setCardNumber("4111111111111111");
        payment.setOrderId(1L);
        PaymentStatus status = adapter.processPayment(payment);
        assertEquals(PaymentStatus.COMPLETED, status);
    }

    @Test
    void testProcessPayment_CardNumberStartsWith5() {
        Payment payment = new Payment();
        payment.setCardNumber("5111111111111111");
        payment.setOrderId(2L);
        PaymentStatus status = adapter.processPayment(payment);
        assertEquals(PaymentStatus.FAILED, status);
    }

    @Test
    void testProcessPayment_RandomCardNumber() {
        Payment payment = new Payment();
        payment.setCardNumber("6111111111111111");
        payment.setOrderId(3L);
        PaymentStatus status = adapter.processPayment(payment);
        assertNotNull(status);
        assertTrue(status == PaymentStatus.COMPLETED || status == PaymentStatus.FAILED);
    }

    @Test
    void testCheckPaymentStatus_ExistingOrderId() {
        Payment payment = new Payment();
        payment.setCardNumber("4111111111111111");
        payment.setOrderId(4L);
        adapter.processPayment(payment);

        PaymentStatus status = adapter.checkPaymentStatus("4");
        assertEquals(PaymentStatus.COMPLETED, status);
    }

    @Test
    void testCheckPaymentStatus_NonExistingOrderId() {
        PaymentStatus status = adapter.checkPaymentStatus("999");
        assertEquals(PaymentStatus.PENDING, status);
    }
}