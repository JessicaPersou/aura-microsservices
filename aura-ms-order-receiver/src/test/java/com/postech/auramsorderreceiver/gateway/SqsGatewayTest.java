// Adicione o seguinte código ao arquivo src/test/java/com/postech/auramsorderreceiver/gateway/SqsGatewayTest.java

package com.postech.auramsorderreceiver.gateway;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorderreceiver.domain.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SqsGatewayTest {

    @Mock
    private AmazonSQS sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsGateway sqsGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOrderToQueue_JsonProcessingException() throws JsonProcessingException {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        when(objectMapper.writeValueAsString(orderRequest)).thenThrow(new JsonProcessingException("Error") {
        });

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> sqsGateway.sendOrderToQueue(orderRequest));
        assertEquals("Error sending message to queue", exception.getMessage());
    }

}