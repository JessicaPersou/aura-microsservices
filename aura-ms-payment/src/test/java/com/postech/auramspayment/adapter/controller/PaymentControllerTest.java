package com.postech.auramspayment.adapter.controller;

import com.postech.auramspayment.adapter.controller.dto.PaymentRequestDTO;
import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.application.GetPaymentStatusUseCase;
import com.postech.auramspayment.application.ProcessPaymentUseCase;
import com.postech.auramspayment.config.exception.ProcessPaymentException;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private ProcessPaymentUseCase processPaymentUseCase;

    @Mock
    private GetPaymentStatusUseCase getPaymentStatusUseCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(1L);
        PaymentResponseDTO response = new PaymentResponseDTO(1L, PaymentStatus.COMPLETED, null, null);

        when(processPaymentUseCase.execute(request)).thenReturn(response);

        // Act
        ResponseEntity<PaymentResponseDTO> result = paymentController.processPayment(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getOrderId());
        assertEquals(PaymentStatus.COMPLETED, result.getBody().getStatus());
    }

    @Test
    void testProcessPayment_BadRequest() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(1L);

        when(processPaymentUseCase.execute(request)).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<PaymentResponseDTO> result = paymentController.processPayment(request);

        // Assert
        assertEquals(400, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getOrderId());
        assertEquals("Invalid data", result.getBody().getMessage());
    }

    @Test
    void testProcessPayment_InternalServerError() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(1L);

        when(processPaymentUseCase.execute(request)).thenThrow(new ProcessPaymentException("Processing error"));

        // Act
        ResponseEntity<PaymentResponseDTO> result = paymentController.processPayment(request);

        // Assert
        assertEquals(500, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getOrderId());
        assertEquals("Processing error", result.getBody().getMessage());
    }
}