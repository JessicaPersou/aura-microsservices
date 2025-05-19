package com.postech.auramsorder.config.messaging;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.adapter.dto.OrderRequestDTO;
import com.postech.auramsorder.application.ProcessOrderUseCase;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SqsConsumer {

    @Value("${aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sqs.order-queue-name}")
    private String queueName;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProcessOrderUseCase processOrderUseCase;

    public SqsConsumer(ProcessOrderUseCase processOrderUseCase) {
        this.processOrderUseCase = processOrderUseCase;
    }

    @PostConstruct
    public void startListening() {
        if (!Boolean.parseBoolean(System.getProperty("sqs.listener.enabled", "true"))) {
            System.out.println("SQS Listener está desativado.");
            return;
        }
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new EndpointConfiguration(sqsEndpoint, region))
                .build();

        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
        System.out.println("Conectado à fila: " + queueUrl);
        System.out.println("Aguardando mensagens...");

        new Thread(() -> {
            try {
                while (true) {
                    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                            .withMaxNumberOfMessages(5)
                            .withWaitTimeSeconds(10);

                    List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

                    if (messages.isEmpty()) {
                        System.out.println("Nenhuma mensagem disponível. Aguardando...");
                        continue;
                    }

                    for (Message message : messages) {
                        try {
                            JsonNode jsonNode = objectMapper.readTree(message.getBody());
                            System.out.println("Mensagem recebida: " + jsonNode.toPrettyString());

                            // Converte o JSON para OrderRequestDTO
                            OrderRequestDTO orderRequestDTO = objectMapper.treeToValue(jsonNode, OrderRequestDTO.class);

                            // Processa o pedido
                            processOrderUseCase.process(orderRequestDTO);

                            // Remove a mensagem da fila após o processamento
                            sqsClient.deleteMessage(queueUrl, message.getReceiptHandle());
                            System.out.println("Mensagem processada e removida da fila.");
                        } catch (Exception e) {
                            System.err.println("Erro ao processar mensagem: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro no consumidor SQS: " + e.getMessage());
            }
        }).start();
    }

}