package com.postech.auramsclient.application;

import com.postech.auramsclient.config.exceptions.DuplicateResourceException;
import com.postech.auramsclient.domain.Address;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    private Client client;
    private ClientEntity clientEntity;
    private CPF cpf;

    @BeforeEach
    void setUp() {
        cpf = new CPF("80080933076");

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(
                "Rua Principal",
                "123",
                "01234567",
                "Centro",
                "São Paulo",
                "SP"
        ));

        client = new Client(
                "John",
                "Doe",
                cpf,
                LocalDate.of(1990, 1, 1),
                addresses
        );

        clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setFirstName("John");
        clientEntity.setLastName("Doe");
        clientEntity.setCpf("80080933076");
        clientEntity.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldCreateClientSuccessfully() {
        when(clientRepository.existsByCpf(anyString())).thenReturn(false);
        when(modelMapper.map(any(Client.class), eq(ClientEntity.class))).thenReturn(clientEntity);
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        when(modelMapper.map(any(ClientEntity.class), eq(Client.class))).thenReturn(client);

        Client result = createClientUseCase.createClient(client);

        assertNotNull(result);
        assertEquals(client.getFirstName(), result.getFirstName());
        assertEquals(client.getLastName(), result.getLastName());
        assertEquals(client.getCpf(), result.getCpf());

        verify(clientRepository).existsByCpf(cpf.getValue());
        verify(modelMapper).map(client, ClientEntity.class);
        verify(clientRepository).save(clientEntity);
        verify(modelMapper).map(clientEntity, Client.class);
    }

    @Test
    void shouldThrowExceptionWhenCpfExists() {
        when(clientRepository.existsByCpf(anyString())).thenReturn(true);

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            createClientUseCase.createClient(client);
        });

        assertEquals("Já existe cliente cadastrado com esse CPF", exception.getMessage());
        verify(clientRepository).existsByCpf(cpf.getValue());
        verifyNoMoreInteractions(clientRepository);
        verifyNoInteractions(modelMapper);
    }
}