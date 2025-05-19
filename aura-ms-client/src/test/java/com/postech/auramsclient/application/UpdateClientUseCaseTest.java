package com.postech.auramsclient.application;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.domain.valueobject.CPF;
import com.postech.auramsclient.gateway.ClientRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateClientUseCase updateClientUseCase;

    private Client client;
    private ClientEntity clientEntity;
    private ClientEntity updatedClientEntity;
    private Client updatedClient;
    private Long clientId;

    @BeforeEach
    void setUp() {
        clientId = 1L;

        client = new Client();
        client.setFirstName("John Updated");
        client.setLastName("Doe Updated");
        client.setCpf(new CPF("80080933076"));
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setFirstName("John");
        clientEntity.setLastName("Doe");
        clientEntity.setCpf("80080933076");
        clientEntity.setBirthDate(LocalDate.of(1990, 1, 1));

        updatedClientEntity = new ClientEntity();
        updatedClientEntity.setId(clientId);
        updatedClientEntity.setFirstName("John Updated");
        updatedClientEntity.setLastName("Doe Updated");
        updatedClientEntity.setCpf("80080933076");
        updatedClientEntity.setBirthDate(LocalDate.of(1990, 1, 1));

        updatedClient = new Client();
        updatedClient.setId(clientId);
        updatedClient.setFirstName("John Updated");
        updatedClient.setLastName("Doe Updated");
        updatedClient.setCpf(new CPF("80080933076"));
        updatedClient.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        when(clientRepository.findById(clientId)).thenReturn(clientEntity);
        when(clientRepository.updateClient(eq(clientId), any(ClientEntity.class))).thenReturn(updatedClientEntity);
        when(modelMapper.map(any(ClientEntity.class), eq(Client.class))).thenReturn(updatedClient);

        Client result = updateClientUseCase.updateClient(clientId, client);

        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe Updated", result.getLastName());

        verify(clientRepository).findById(clientId);
        verify(clientRepository).updateClient(eq(clientId), any(ClientEntity.class));
        verify(modelMapper).map(updatedClientEntity, Client.class);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.findById(clientId)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            updateClientUseCase.updateClient(clientId, client);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientRepository).findById(clientId);
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(modelMapper);
    }
}