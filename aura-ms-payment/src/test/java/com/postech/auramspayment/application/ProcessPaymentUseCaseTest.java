package com.postech.auramspayment.application;

import com.postech.auramspayment.adapter.controller.dto.PaymentRequestDTO;
import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.config.exception.ProcessPaymentException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGatewayAdapter paymentGatewayAdapter;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    private ProcessPaymentUseCase processPaymentUseCase;

    @BeforeEach
    void setUp() {
        processPaymentUseCase = new ProcessPaymentUseCase(paymentRepository, paymentGatewayAdapter);
    }

    @Test
    void shouldProcessPaymentSuccessfully() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, "4111111111111111", new BigDecimal("100.00"), 1L);

        Payment savedPayment = new Payment(1L, 123L, "4111111111111111",
                new BigDecimal("100.00"), PaymentStatus.COMPLETED,
                LocalDateTime.now(), LocalDateTime.now());

        when(paymentGatewayAdapter.processPayment(any(Payment.class))).thenReturn(PaymentStatus.COMPLETED);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // When
        PaymentResponseDTO response = processPaymentUseCase.execute(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(123L, response.getOrderId());
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        assertTrue(response.getMessage().contains("sucesso"));
        assertNotNull(response.getProcessedAt());

        verify(paymentGatewayAdapter).processPayment(paymentCaptor.capture());
        verify(paymentRepository).save(any(Payment.class));

        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(requestDTO.getOrderId(), capturedPayment.getOrderId());
        assertEquals(requestDTO.getCardNumber(), capturedPayment.getCardNumber());
        assertEquals(requestDTO.getAmount(), capturedPayment.getAmount());
    }

    @Test
    void shouldHandleFailedPayment() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, "5111111111111111", new BigDecimal("100.00"), 1L);

        Payment savedPayment = new Payment(1L, 123L, "5111111111111111",
                new BigDecimal("100.00"), PaymentStatus.FAILED,
                LocalDateTime.now(), LocalDateTime.now());

        when(paymentGatewayAdapter.processPayment(any(Payment.class))).thenReturn(PaymentStatus.FAILED);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // When
        PaymentResponseDTO response = processPaymentUseCase.execute(requestDTO);

        // Then
        assertNotNull(response);
        assertEquals(123L, response.getOrderId());
        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertTrue(response.getMessage().contains("Falha"));
        assertNotNull(response.getProcessedAt());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processPaymentUseCase.execute(null);
        });

        assertEquals("O request de pagamento não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(null, "4111111111111111", new BigDecimal("100.00"), 1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processPaymentUseCase.execute(requestDTO);
        });

        assertTrue(exception.getMessage().contains("ID do pedido não pode ser vazio"));
    }

    @Test
    void shouldThrowExceptionWhenCardNumberIsNull() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, null, new BigDecimal("100.00"), 1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processPaymentUseCase.execute(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Número do cartão não pode ser vazio"));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, "4111111111111111", null, 1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processPaymentUseCase.execute(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Valor do pagamento deve ser maior que zero"));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, "4111111111111111", BigDecimal.ZERO, 1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            processPaymentUseCase.execute(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Valor do pagamento deve ser maior que zero"));
    }

    @Test
    void shouldWrapExceptionFromGateway() {
        // Given
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(123L, "4111111111111111", new BigDecimal("100.00"), 1L);

        when(paymentGatewayAdapter.processPayment(any(Payment.class))).thenThrow(new RuntimeException("Gateway error"));

        // When & Then
        ProcessPaymentException exception = assertThrows(ProcessPaymentException.class, () -> {
            processPaymentUseCase.execute(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Erro ao processar pagamento"));
        assertTrue(exception.getMessage().contains("Gateway error"));
    }
}