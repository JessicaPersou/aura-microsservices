package com.postech.auramsorder.gateway.client;

import com.postech.auramsorder.adapter.dto.ClientDTO;
import com.postech.auramsorder.config.exception.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {
    @Value("${client.service.url}")
    private String clientServiceUrl;

    public String getClientServiceUrl() {
        return clientServiceUrl;
    }

    public void setClientServiceUrl(String clientServiceUrl) {
        this.clientServiceUrl = clientServiceUrl;
    }

    private final RestTemplate restTemplate;

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ClientDTO verifyClient(Long clientId) {
        try {
            ResponseEntity<ClientDTO> response =
                    restTemplate.getForEntity(clientServiceUrl + clientId, ClientDTO.class);
            return response.getBody();
        } catch (Exception e) {
            throw new ClientNotFoundException("Cliente nao encontrado: " + clientId);
        }
    }
}