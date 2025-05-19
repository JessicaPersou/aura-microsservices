package com.postech.auramsclient.adapters;

import com.postech.auramsclient.adapters.dto.AddressDTO;
import com.postech.auramsclient.adapters.dto.ClientDTO;
import com.postech.auramsclient.application.CreateClientUseCase;
import com.postech.auramsclient.application.DeleteClientUseCase;
import com.postech.auramsclient.application.FindClientUseCase;
import com.postech.auramsclient.application.UpdateClientUseCase;
import com.postech.auramsclient.application.address.CreateAddressUseCase;
import com.postech.auramsclient.application.address.DeleteAddressUseCase;
import com.postech.auramsclient.application.address.UpdateAddressUseCase;
import com.postech.auramsclient.config.exceptions.DuplicateResourceException;
import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.domain.Address;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.domain.valueobject.CPF;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private CreateClientUseCase createClientUseCase;

    @Mock
    private FindClientUseCase findClientUseCase;

    @Mock
    private UpdateClientUseCase updateClientUseCase;

    @Mock
    private DeleteClientUseCase deleteClientUseCase;

    @Mock
    private CreateAddressUseCase createAddressUseCase;

    @Mock
    private UpdateAddressUseCase updateAddressUseCase;

    @Mock
    private DeleteAddressUseCase deleteAddressUseCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO clientDTO;
    private Client client;
    private AddressDTO addressDTO;
    private Address address;
    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        // Setup AddressDTO
        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Rua Principal");
        addressDTO.setNumber("123");
        addressDTO.setZipcode("01234567");
        addressDTO.setNeighborhood("Centro");
        addressDTO.setCity("São Paulo");
        addressDTO.setState("SP");

        // Setup ClientDTO
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);

        clientDTO = new ClientDTO();
        clientDTO.setClientId(1L);
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setCpf("80080933076");
        clientDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clientDTO.setAddresses(addressDTOList);

        // Setup Address
        address = new Address();
        address.setId(1L);
        address.setStreet("Rua Principal");
        address.setNumber("123");
        address.setZipcode("01234567");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");

        // Setup Client
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setCpf(new CPF("80080933076"));
        client.setBirthDate(LocalDate.of(1990, 1, 1));
        client.setAddresses(addresses);

        // Setup AddressEntity
        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua Principal");
        addressEntity.setNumber("123");
        addressEntity.setZipcode("01234567");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");
    }

    @Test
    void shouldCreateClientSuccessfully() {
        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(createClientUseCase.createClient(any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.createClient(clientDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getClientId());
        assertEquals("John", response.getBody().getFirstName());

        verify(modelMapper).map(clientDTO, Client.class);
        verify(createClientUseCase).createClient(client);
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void shouldReturnConflictWhenClientWithCpfExists() {
        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(createClientUseCase.createClient(any(Client.class))).thenThrow(new DuplicateResourceException("Já existe cliente cadastrado com esse CPF"));

        ResponseEntity<ClientDTO> response = clientController.createClient(clientDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());

        verify(modelMapper).map(clientDTO, Client.class);
        verify(createClientUseCase).createClient(client);
    }

    @Test
    void shouldListAllClients() {
        List<Client> clients = Collections.singletonList(client);

        when(findClientUseCase.findAll()).thenReturn(clients);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<List<ClientDTO>> response = clientController.listAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getClientId());

        verify(findClientUseCase).findAll();
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void shouldFindClientById() {
        Long clientId = 1L;

        when(findClientUseCase.findById(clientId)).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.findClientById(clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getClientId());

        verify(findClientUseCase).findById(clientId);
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void shouldFindClientByDocument() {
        String cpf = "80080933076";

        when(findClientUseCase.findByCpf(cpf)).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.findClientByDocument(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getClientId());
        assertEquals(cpf, response.getBody().getCpf());

        verify(findClientUseCase).findByCpf(cpf);
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void shouldUpdateClient() {
        Long clientId = 1L;

        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(updateClientUseCase.updateClient(eq(clientId), any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.updateClient(clientId, clientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getClientId());

        verify(modelMapper).map(clientDTO, Client.class);
        verify(updateClientUseCase).updateClient(clientId, client);
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void shouldHandleResourceNotFoundExceptionWhenUpdatingClient() {
        Long clientId = 999L;

        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(updateClientUseCase.updateClient(eq(clientId), any(Client.class))).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> {
            clientController.updateClient(clientId, clientDTO);
        });

        verify(modelMapper).map(clientDTO, Client.class);
        verify(updateClientUseCase).updateClient(clientId, client);
    }

    @Test
    void shouldDeleteClient() {
        Long clientId = 1L;

        doNothing().when(deleteClientUseCase).deleteClient(clientId);

        ResponseEntity<Void> response = clientController.deleteClient(clientId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteClientUseCase).deleteClient(clientId);
    }

    @Test
    void shouldHandleResourceNotFoundExceptionWhenDeletingClient() {
        Long clientId = 999L;

        doThrow(new ResourceNotFoundException("Cliente não encontrado")).when(deleteClientUseCase).deleteClient(clientId);

        assertThrows(ResourceNotFoundException.class, () -> {
            clientController.deleteClient(clientId);
        });

        verify(deleteClientUseCase).deleteClient(clientId);
    }

    @Test
    void shouldAddAddressToClient() {
        Long clientId = 1L;

        when(modelMapper.map(any(AddressDTO.class), eq(AddressEntity.class))).thenReturn(addressEntity);
        when(createAddressUseCase.createAddress(eq(clientId), any(AddressEntity.class))).thenReturn(addressEntity);
        when(modelMapper.map(any(AddressEntity.class), eq(AddressDTO.class))).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = clientController.addAddressToClient(clientId, addressDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(modelMapper).map(addressDTO, AddressEntity.class);
        verify(createAddressUseCase).createAddress(clientId, addressEntity);
        verify(modelMapper).map(addressEntity, AddressDTO.class);
    }

    @Test
    void shouldEditAddress() {
        Long clientId = 1L;
        Long addressId = 1L;

        when(modelMapper.map(any(AddressDTO.class), eq(AddressEntity.class))).thenReturn(addressEntity);
        when(updateAddressUseCase.updateAddress(eq(clientId), eq(addressId), any(AddressEntity.class))).thenReturn(addressEntity);
        when(modelMapper.map(any(AddressEntity.class), eq(AddressDTO.class))).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = clientController.editAddress(clientId, addressId, addressDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(modelMapper).map(addressDTO, AddressEntity.class);
        verify(updateAddressUseCase).updateAddress(clientId, addressId, addressEntity);
        verify(modelMapper).map(addressEntity, AddressDTO.class);
    }

    @Test
    void shouldDeleteAddress() {
        Long clientId = 1L;
        Long addressId = 1L;

        doNothing().when(deleteAddressUseCase).deleteAddress(clientId, addressId);

        ResponseEntity<Void> response = clientController.deleteClient(clientId, addressId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteAddressUseCase).deleteAddress(clientId, addressId);
    }
}