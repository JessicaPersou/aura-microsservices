package com.postech.auramspayment.gateway;

import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.gateway.database.jpa.entity.PaymentEntity;
import com.postech.auramspayment.gateway.database.jpa.repository.PaymentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentRepositoryImplTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Payment payment = new Payment();
        PaymentEntity paymentEntity = new PaymentEntity();
        PaymentEntity savedEntity = new PaymentEntity();
        Payment savedPayment = new Payment();

        when(modelMapper.map(payment, PaymentEntity.class)).thenReturn(paymentEntity);
        when(paymentJpaRepository.save(paymentEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, Payment.class)).thenReturn(savedPayment);

        Payment result = paymentRepository.save(payment);

        assertNotNull(result);
        assertEquals(savedPayment, result);
        verify(modelMapper).map(payment, PaymentEntity.class);
        verify(paymentJpaRepository).save(paymentEntity);
        verify(modelMapper).map(savedEntity, Payment.class);
    }

    @Test
    void testFindByOrderId_Found() {
        Long orderId = 1L;
        PaymentEntity paymentEntity = new PaymentEntity();
        Payment payment = new Payment();

        when(paymentJpaRepository.findByOrderId(orderId)).thenReturn(paymentEntity);
        when(modelMapper.map(paymentEntity, Payment.class)).thenReturn(payment);

        Optional<Payment> result = paymentRepository.findByOrderId(orderId);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
        verify(paymentJpaRepository).findByOrderId(orderId);
        verify(modelMapper).map(paymentEntity, Payment.class);
    }

    @Test
    void testFindByOrderId_NotFound() {
        Long orderId = 1L;

        when(paymentJpaRepository.findByOrderId(orderId)).thenReturn(null);

        Optional<Payment> result = paymentRepository.findByOrderId(orderId);

        assertFalse(result.isPresent());
        verify(paymentJpaRepository).findByOrderId(orderId);
        verifyNoInteractions(modelMapper);
    }
}