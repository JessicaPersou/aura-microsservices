package com.postech.auramsorder.gateway.client;

import com.postech.auramsorder.adapter.dto.ClientDTO;
import com.postech.auramsorder.config.exception.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ClientService clientService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService.setClientServiceUrl("http://localhost:8004/api/v1/clients/");
    }

    @Test
    void verifyClient_ShouldReturnClientDTO_WhenClientExists() {
        Long clientId = 1L;
        String clientServiceUrl = clientService.getClientServiceUrl() + clientId;
        ClientDTO mockClientDTO = new ClientDTO(clientId, "Nome", "Sobrenome", "57187365020");

        when(restTemplate.getForEntity(clientServiceUrl, ClientDTO.class))
                .thenReturn(new ResponseEntity<>(mockClientDTO, HttpStatus.OK));

        ClientDTO result = clientService.verifyClient(clientId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientId, result.getClientId()),
                () -> assertEquals("Nome", result.getFirstName()),
                () -> assertEquals("Sobrenome", result.getLastName()),
                () -> assertEquals("57187365020", result.getCpf())
        );
    }

    @Test
    void verifyClient_ShouldThrowClientNotFoundException_WhenClientDoesNotExist() {
        Long clientId = 1L;
        String clientServiceUrl = clientService.getClientServiceUrl() + clientId;

        when(restTemplate.getForEntity(clientServiceUrl, ClientDTO.class))
                .thenThrow(new RuntimeException("Cliente nao encontrado"));

        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.verifyClient(clientId);
        });

        assertEquals("Cliente nao encontrado: " + clientId, exception.getMessage());
    }
}