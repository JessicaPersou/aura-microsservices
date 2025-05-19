package com.postech.auramsclient.gateway.database.jpa.repository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientJpaRepositoryTest {

    @Mock
    private ClientJpaRepository clientJpaRepository;

    private String cpf;
    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        cpf = "12345678900";
        clientEntity = new ClientEntity();
        clientEntity.setCpf(cpf);
    }
    @Test
    void shouldCheckIfClientExistsByCpf() {
        when(clientJpaRepository.existsByCpf(cpf)).thenReturn(true);

        boolean result = clientJpaRepository.existsByCpf(cpf);

        assertTrue(result);
        verify(clientJpaRepository).existsByCpf(cpf);
    }
    @Test
    void shouldFindClientByCpf() {
        when(clientJpaRepository.findByCpf(cpf)).thenReturn(clientEntity);

        ClientEntity result = clientJpaRepository.findByCpf(cpf);

        assertEquals(clientEntity, result);
        verify(clientJpaRepository).findByCpf(cpf);
    }
    @Test
    void shouldReturnFalseWhenClientDoesNotExistByCpf() {
        String nonExistentCpf = "12345678901";
        when(clientJpaRepository.existsByCpf(nonExistentCpf)).thenReturn(false);

        boolean result = clientJpaRepository.existsByCpf(nonExistentCpf);

        assertFalse(result);
        verify(clientJpaRepository).existsByCpf(nonExistentCpf);
    }

    @Test
    void shouldReturnNullWhenClientNotFoundByCpf() {
        String nonExistentCpf = "12345678901";
        when(clientJpaRepository.findByCpf(nonExistentCpf)).thenReturn(null);

        ClientEntity result = clientJpaRepository.findByCpf(nonExistentCpf);

        assertNull(result);
        verify(clientJpaRepository).findByCpf(nonExistentCpf);
    }
}