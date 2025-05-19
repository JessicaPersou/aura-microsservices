package com.postech.auramsorderreceiver.gateway;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorderreceiver.domain.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqsGateway {

    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.order-queue-name:new-order-queue}")
    private String queueName;

    @Autowired
    public SqsGateway(AmazonSQS sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    public String sendOrderToQueue(OrderRequest orderRequest) {
        try {
            String orderJson = objectMapper.writeValueAsString(orderRequest);
            String queueUrl = getQueueUrl();

            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(orderJson);

            SendMessageResult result = sqsClient.sendMessage(sendMessageRequest);
            log.info("Message sent to SQS. Message ID: {}", result.getMessageId(), "Queue URL: {}", queueUrl);

            return result.getMessageId();
        } catch (JsonProcessingException e) {
            log.error("Failed to convert order to JSON", e);
            throw new RuntimeException("Error sending message to queue", e);
        }
    }

    private String getQueueUrl() {
        GetQueueUrlRequest getQueueRequest = new GetQueueUrlRequest()
                .withQueueName(queueName);
        GetQueueUrlResult result = sqsClient.getQueueUrl(getQueueRequest);
        return result.getQueueUrl();
    }
}