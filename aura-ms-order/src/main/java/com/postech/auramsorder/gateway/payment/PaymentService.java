package com.postech.auramsorder.gateway.payment;

import com.postech.auramsorder.adapter.dto.PaymentRequestDTO;
import com.postech.auramsorder.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    private final RestTemplate restTemplate;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public String getPaymentServiceUrl() {
        return paymentServiceUrl;
    }

    public void setPaymentServiceUrl(String paymentServiceUrl) {
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean processPayment(Order orderForPayment) {
        try {
            PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
            paymentRequest.setOrderId(orderForPayment.getId());
            paymentRequest.setCardNumber(orderForPayment.getPaymentCardNumber());
            paymentRequest.setAmount(orderForPayment.getTotalAmount());
            paymentRequest.setClientId(orderForPayment.getClientId());

            ResponseEntity<PaymentRequestDTO> response =
                    restTemplate.postForEntity(paymentServiceUrl + "process", paymentRequest, PaymentRequestDTO.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao processar pagamento para o pedido: " + orderForPayment.getId(), e);
            return false;
        }
    }

    public void refusedIfNecessary(Order order) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("orderId", order.getId());
            request.put("clientId", order.getClientId());
            request.put("amount", order.getTotalAmount());

            restTemplate.postForEntity(paymentServiceUrl + "/refuse", request, Void.class);
            LOGGER.info("Pagamento recusado com sucesso para o pedido: " + order.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao recusar pagamento para o pedido: " + order.getId(), e);
        }
    }
}