package com.postech.auramsclient.application;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.gateway.ClientRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DeleteClientUseCase deleteClientUseCase;

    @Test
    void shouldDeleteClientSuccessfully() {
        Long clientId = 1L;
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(clientEntity);
        doNothing().when(clientRepository).deleteById(clientId);

        deleteClientUseCase.deleteClient(clientId);

        verify(clientRepository).findById(clientId);
        verify(clientRepository).deleteById(clientId);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteClientUseCase.deleteClient(clientId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientRepository).findById(clientId);
        verifyNoMoreInteractions(clientRepository);
    }
}