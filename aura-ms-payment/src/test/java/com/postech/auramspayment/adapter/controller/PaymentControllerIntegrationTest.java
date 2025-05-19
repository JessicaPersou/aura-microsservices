package com.postech.auramspayment.adapter.controller;

import com.postech.auramspayment.adapter.controller.dto.PaymentRequestDTO;
import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.application.ProcessPaymentUseCase;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import com.postech.auramspayment.gateway.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProcessPaymentUseCase processPaymentUseCase;

    @Test
    void testProcessPayment_Success() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setOrderId(1L);
        request.setCardNumber("1234567890123456");
        request.setAmount(new BigDecimal("100.00"));
        request.setClientId(1L);

        // Act
        ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(
                "/api/v1/payments/process", request, PaymentResponseDTO.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getOrderId());
        assertEquals(PaymentStatus.COMPLETED, response.getBody().getStatus());
    }

    @Test
    void testProcessPayment_BadRequest() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO(); // Dados inválidos

        // Act
        ResponseEntity<PaymentResponseDTO> response = restTemplate.postForEntity(
                "/api/v1/payments/process", request, PaymentResponseDTO.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
    }

}