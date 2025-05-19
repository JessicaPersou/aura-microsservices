package com.postech.auramsclient.application;

import com.postech.auramsclient.domain.Client;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FindClientUseCase findClientUseCase;

    private ClientEntity clientEntity1;
    private ClientEntity clientEntity2;
    private Client client1;
    private Client client2;
    private List<ClientEntity> clientEntities;

    @BeforeEach
    void setUp() {
        clientEntity1 = new ClientEntity();
        clientEntity1.setId(1L);
        clientEntity1.setFirstName("John");
        clientEntity1.setLastName("Doe");
        clientEntity1.setCpf("80080933076");
        clientEntity1.setBirthDate(LocalDate.of(1990, 1, 1));

        clientEntity2 = new ClientEntity();
        clientEntity2.setId(2L);
        clientEntity2.setFirstName("Jane");
        clientEntity2.setLastName("Smith");
        clientEntity2.setCpf("12087670030");
        clientEntity2.setBirthDate(LocalDate.of(1985, 5, 15));

        client1 = new Client();
        client1.setId(1L);
        client1.setFirstName("John");
        client1.setLastName("Doe");

        client2 = new Client();
        client2.setId(2L);
        client2.setFirstName("Jane");
        client2.setLastName("Smith");

        clientEntities = new ArrayList<>();
        clientEntities.add(clientEntity1);
        clientEntities.add(clientEntity2);
    }

    @Test
    void shouldFindAllClients() {
        when(clientRepository.findAll()).thenReturn(clientEntities);
        when(modelMapper.map(eq(clientEntity1), eq(Client.class))).thenReturn(client1);
        when(modelMapper.map(eq(clientEntity2), eq(Client.class))).thenReturn(client2);

        List<Client> result = findClientUseCase.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(clientRepository).findAll();
        verify(modelMapper).map(clientEntity1, Client.class);
        verify(modelMapper).map(clientEntity2, Client.class);
    }

    @Test
    void shouldFindClientById() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(clientEntity1);
        when(modelMapper.map(any(ClientEntity.class), eq(Client.class))).thenReturn(client1);

        Client result = findClientUseCase.findById(id);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(clientRepository).findById(id);
        verify(modelMapper).map(clientEntity1, Client.class);
    }

    @Test
    void shouldFindClientByCpf() {
        String cpf = "80080933076";
        when(clientRepository.findByCpf(cpf)).thenReturn(clientEntity1);
        when(modelMapper.map(any(ClientEntity.class), eq(Client.class))).thenReturn(client1);

        Client result = findClientUseCase.findByCpf(cpf);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(clientRepository).findByCpf(cpf);
        verify(modelMapper).map(clientEntity1, Client.class);
    }
}