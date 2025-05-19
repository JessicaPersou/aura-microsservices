package com.postech.auramsorderreceiver.application;

import com.postech.auramsorderreceiver.domain.OrderRequest;
import com.postech.auramsorderreceiver.gateway.SqsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveOrderUseCase {

    private final SqsGateway sqsGateway;

    public String receiveOrder(OrderRequest orderRequest) {
        validateOrder(orderRequest);

        log.info("Receiving order for client: {}", orderRequest.getClientId());
        String messageId = sqsGateway.sendOrderToQueue(orderRequest);

        log.info("Order received and sent to processing queue. Message ID: {}", messageId);
        return messageId;
    }

    private void validateOrder(OrderRequest orderRequest) {
        if (orderRequest.getClientId() == null) {
            throw new IllegalArgumentException("Client ID must be provided");
        }

        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        if (orderRequest.getPaymentData() == null ||
                orderRequest.getPaymentData().getCreditCardNumber() == null ||
                orderRequest.getPaymentData().getCreditCardNumber().isEmpty()) {
            throw new IllegalArgumentException("Payment data must be provided");
        }
    }
}
