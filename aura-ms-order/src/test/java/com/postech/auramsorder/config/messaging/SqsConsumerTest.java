package com.postech.auramsorder.config.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.application.ProcessOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SqsConsumerTest {

    @Mock
    private AmazonSQS amazonSQS;

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SqsConsumer sqsConsumer;

    private final String queueUrl = "http://test-queue-url";
    private final String queueName = "test-queue";
    private final String region = "us-east-1";
    private final String accessKey = "test-access-key";
    private final String secretKey = "test-secret-key";
    private final String sqsEndpoint = "http://test-endpoint";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sqsConsumer, "sqsEndpoint", sqsEndpoint);
        ReflectionTestUtils.setField(sqsConsumer, "accessKey", accessKey);
        ReflectionTestUtils.setField(sqsConsumer, "secretKey", secretKey);
        ReflectionTestUtils.setField(sqsConsumer, "region", region);
        ReflectionTestUtils.setField(sqsConsumer, "queueName", queueName);

        // Injetar o ObjectMapper mockado
        ReflectionTestUtils.setField(sqsConsumer, "objectMapper", objectMapper);
    }


    @Test
    void startListening_ShouldNotStartWhenDisabled() {
        // Arrange
        System.setProperty("sqs.listener.enabled", "false");

        // Act
        sqsConsumer.startListening();

        // Assert
        verify(amazonSQS, never()).getQueueUrl(anyString());
        verify(amazonSQS, never()).receiveMessage(anyString());
    }
}