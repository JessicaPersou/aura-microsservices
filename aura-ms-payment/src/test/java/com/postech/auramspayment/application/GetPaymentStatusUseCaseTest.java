package com.postech.auramspayment.application;

import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import com.postech.auramspayment.gateway.PaymentRepository;
import com.postech.auramspayment.gateway.external.PaymentGatewayAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPaymentStatusUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGatewayAdapter paymentGatewayAdapter;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    private GetPaymentStatusUseCase getPaymentStatusUseCase;

    @BeforeEach
    void setUp() {
        getPaymentStatusUseCase = new GetPaymentStatusUseCase(paymentRepository, paymentGatewayAdapter, modelMapper);
    }

    @Test
    void shouldGetCompletedPaymentStatus() {
        // Given
        Long orderId = 123L;
        Payment payment = new Payment(1L, orderId, "4111111111111111",
                new BigDecimal("100.00"), PaymentStatus.COMPLETED,
                LocalDateTime.now(), LocalDateTime.now());

        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));

        // When
        PaymentResponseDTO response = getPaymentStatusUseCase.execute(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        assertTrue(response.getMessage().contains("concluído com sucesso"));
        assertNotNull(response.getProcessedAt());

        // No need to check gateway for completed payments
        verify(paymentGatewayAdapter, never()).checkPaymentStatus(any());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldCheckGatewayForPendingPayment() {
        // Given
        Long orderId = 123L;
        Payment payment = new Payment(1L, orderId, "4111111111111111",
                new BigDecimal("100.00"), PaymentStatus.PENDING,
                LocalDateTime.now().minusMinutes(5), LocalDateTime.now().minusMinutes(5));

        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));
        when(paymentGatewayAdapter.checkPaymentStatus(orderId.toString())).thenReturn(PaymentStatus.COMPLETED);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        PaymentResponseDTO response = getPaymentStatusUseCase.execute(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        assertTrue(response.getMessage().contains("concluído com sucesso"));

        verify(paymentGatewayAdapter).checkPaymentStatus(orderId.toString());
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(PaymentStatus.COMPLETED, savedPayment.getStatus());
        assertTrue(savedPayment.getUpdatedAt().isAfter(payment.getCreatedAt()));
    }

    @Test
    void shouldReturnNotFoundResponseWhenPaymentDoesNotExist() {
        // Given
        Long orderId = 456L;
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        // When
        PaymentResponseDTO response = getPaymentStatusUseCase.execute(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.PENDING, response.getStatus());
        assertTrue(response.getMessage().contains("não encontrado"));
        assertNotNull(response.getProcessedAt());

        verify(paymentGatewayAdapter, never()).checkPaymentStatus(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            getPaymentStatusUseCase.execute(null);
        });

        assertTrue(exception.getMessage().contains("ID do pedido não pode ser vazio"));
    }

    @Test
    void shouldNotUpdatePaymentStatusWhenStillPending() {
        // Given
        Long orderId = 123L;
        Payment payment = new Payment(1L, orderId, "4111111111111111",
                new BigDecimal("100.00"), PaymentStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now());

        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));
        when(paymentGatewayAdapter.checkPaymentStatus(orderId.toString())).thenReturn(PaymentStatus.PENDING);

        // When
        PaymentResponseDTO response = getPaymentStatusUseCase.execute(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.PENDING, response.getStatus());
        assertTrue(response.getMessage().contains("processamento"));

        verify(paymentGatewayAdapter).checkPaymentStatus(orderId.toString());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldHandleFailedPaymentStatus() {
        // Given
        Long orderId = 123L;
        Payment payment = new Payment(1L, orderId, "5111111111111111",
                new BigDecimal("100.00"), PaymentStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now());

        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(payment));
        when(paymentGatewayAdapter.checkPaymentStatus(orderId.toString())).thenReturn(PaymentStatus.FAILED);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        PaymentResponseDTO response = getPaymentStatusUseCase.execute(orderId);

        // Then
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertTrue(response.getMessage().contains("Falha"));

        verify(paymentGatewayAdapter).checkPaymentStatus(orderId.toString());
        verify(paymentRepository).save(any());
    }
}