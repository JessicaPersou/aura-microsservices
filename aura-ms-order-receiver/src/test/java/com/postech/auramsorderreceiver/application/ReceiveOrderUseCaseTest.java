package com.postech.auramsorderreceiver.application;

import com.postech.auramsorderreceiver.domain.OrderItem;
import com.postech.auramsorderreceiver.domain.OrderRequest;
import com.postech.auramsorderreceiver.domain.PaymentData;
import com.postech.auramsorderreceiver.gateway.SqsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiveOrderUseCaseTest {

    @Mock
    private SqsGateway sqsGateway;

    @InjectMocks
    private ReceiveOrderUseCase receiveOrderUseCase;

    private OrderRequest validOrderRequest;

    @BeforeEach
    void setUp() {
        OrderItem item = OrderItem.builder()
                .sku("SKU123")
                .quantity(2)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .creditCardNumber("1234-5678-9012-3456")
                .build();

        validOrderRequest = OrderRequest.builder()
                .clientId(1L)
                .items(List.of(item))
                .paymentData(paymentData)
                .build();
    }

    @Test
    void shouldReceiveOrderSuccessfully() {
        String expectedMessageId = "msg-123456";
        when(sqsGateway.sendOrderToQueue(any(OrderRequest.class))).thenReturn(expectedMessageId);

        String messageId = receiveOrderUseCase.receiveOrder(validOrderRequest);

        assertEquals(expectedMessageId, messageId);
    }

    @Test
    void shouldThrowExceptionWhenClientIdIsNull() {
        validOrderRequest.setClientId(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiveOrderUseCase.receiveOrder(validOrderRequest);
        });

        assertEquals("Client ID must be provided", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenItemsAreEmpty() {
        validOrderRequest.setItems(Collections.emptyList());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiveOrderUseCase.receiveOrder(validOrderRequest);
        });

        assertEquals("Order must have at least one item", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPaymentDataIsNull() {
        validOrderRequest.setPaymentData(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiveOrderUseCase.receiveOrder(validOrderRequest);
        });

        assertEquals("Payment data must be provided", exception.getMessage());
    }
}