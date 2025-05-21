package com.postech.auramsorder.gateway.payment;

import com.postech.auramsorder.adapter.dto.PaymentRequestDTO;
import com.postech.auramsorder.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService.setPaymentServiceUrl("http://mock-payment-service/");
    }

    @Test
    void testProcessPayment_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentCardNumber("1234567890123456");
        order.setTotalAmount(new BigDecimal(200.0));
        order.setClientId(10L);

        when(restTemplate.postForEntity(anyString(), any(PaymentRequestDTO.class), eq(PaymentRequestDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        boolean result = paymentService.processPayment(order);

        assertTrue(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(PaymentRequestDTO.class), eq(PaymentRequestDTO.class));
    }

    @Test
    void testProcessPayment_Failure() {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentCardNumber("1234567890123456");
        order.setTotalAmount(new BigDecimal(200.0));
        order.setClientId(10L);

        when(restTemplate.postForEntity(anyString(), any(PaymentRequestDTO.class), eq(PaymentRequestDTO.class)))
                .thenThrow(new RuntimeException("Erro ao processar pagamento"));

        boolean result = paymentService.processPayment(order);

        assertFalse(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(PaymentRequestDTO.class), eq(PaymentRequestDTO.class));
    }

    @Test
    void testRefusedIfNecessary_Success() {
        when(restTemplate.postForEntity(anyString(), anyMap(), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("200.0"));
        order.setClientId(10L);

        assertDoesNotThrow(() -> paymentService.refusedIfNecessary(order));
        verify(restTemplate, times(1)).postForEntity(anyString(), anyMap(), eq(Void.class));
    }

    @Test
    void testRefusedIfNecessary_Failure() {
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal(200.0));
        order.setClientId(10L);

        doThrow(new RuntimeException("Erro ao recusar pagamento"))
                .when(restTemplate).postForEntity(anyString(), anyMap(), eq(Void.class));

        assertDoesNotThrow(() -> paymentService.refusedIfNecessary(order));
        verify(restTemplate, times(1)).postForEntity(anyString(), anyMap(), eq(Void.class));
    }
}