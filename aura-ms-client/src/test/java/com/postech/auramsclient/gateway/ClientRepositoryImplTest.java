package com.postech.auramsclient.gateway;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import com.postech.auramsclient.gateway.database.jpa.repository.ClientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryImplTest {

    @Mock
    private ClientJpaRepository clientJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientRepositoryImpl clientRepository;

    private ClientEntity clientEntity;
    private Long clientId;
    private String cpf;

    @BeforeEach
    void setUp() {
        clientId = 1L;
        cpf = "80080933076";

        clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setFirstName("John");
        clientEntity.setLastName("Doe");
        clientEntity.setCpf(cpf);
        clientEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        clientEntity.setAddresses(new ArrayList<>());
    }

    @Test
    void shouldCheckIfClientExistsByCpf() {
        when(clientJpaRepository.existsByCpf(cpf)).thenReturn(true);

        boolean result = clientRepository.existsByCpf(cpf);

        assertTrue(result);
        verify(clientJpaRepository).existsByCpf(cpf);
    }

    @Test
    void shouldSaveClient() {
        when(clientJpaRepository.save(clientEntity)).thenReturn(clientEntity);

        ClientEntity result = clientRepository.save(clientEntity);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(cpf, result.getCpf());

        verify(clientJpaRepository).save(clientEntity);
    }

    @Test
    void shouldReturnNullWhenSavingNullClient() {
        ClientEntity result = clientRepository.save(null);

        assertNull(result);
        verifyNoInteractions(clientJpaRepository);
    }

    @Test
    void shouldFindAllClients() {
        List<ClientEntity> clients = new ArrayList<>();
        clients.add(clientEntity);

        when(clientJpaRepository.findAll()).thenReturn(clients);

        List<ClientEntity> result = clientRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientId, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());

        verify(clientJpaRepository).findAll();
    }

    @Test
    void shouldFindClientById() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));

        ClientEntity result = clientRepository.findById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(cpf, result.getCpf());

        verify(clientJpaRepository).findById(clientId);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundById() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.findById(clientId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundByCpf() {
        when(clientJpaRepository.findByCpf(cpf)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.findByCpf(cpf);
        });

        assertEquals("Cliente não encontrado com o CPF: " + cpf, exception.getMessage());
        verify(clientJpaRepository).findByCpf(cpf);
    }

    @Test
    void shouldThrowExceptionWhenCpfIsNull() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.findByCpf(null);
        });

        assertEquals("CPF não pode ser nulo ou vazio", exception.getMessage());
        verifyNoInteractions(clientJpaRepository);
    }

    @Test
    void shouldThrowExceptionWhenCpfIsEmpty() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.findByCpf("");
        });

        assertEquals("CPF não pode ser nulo ou vazio", exception.getMessage());
        verifyNoInteractions(clientJpaRepository);
    }

    @Test
    void shouldUpdateClient() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(clientJpaRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        ClientEntity result = clientRepository.updateClient(clientId, clientEntity);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(cpf, result.getCpf());

        verify(clientJpaRepository).findById(clientId);
        verify(clientJpaRepository).save(any(ClientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.updateClient(clientId, clientEntity);
        });

        assertEquals("Cliente com ID " + clientId + " não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verifyNoMoreInteractions(clientJpaRepository);
    }

    @Test
    void shouldDeleteClientById() {
        when(clientJpaRepository.existsById(clientId)).thenReturn(true);
        doNothing().when(clientJpaRepository).deleteById(clientId);

        clientRepository.deleteById(clientId);

        verify(clientJpaRepository).existsById(clientId);
        verify(clientJpaRepository).deleteById(clientId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentClient() {
        when(clientJpaRepository.existsById(clientId)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientRepository.deleteById(clientId);
        });

        assertEquals("Cliente com ID " + clientId + " não encontrado", exception.getMessage());
        verify(clientJpaRepository).existsById(clientId);
        verifyNoMoreInteractions(clientJpaRepository);
    }
}