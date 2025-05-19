package com.postech.auramsclient.adapters;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.postech.auramsclient.domain.Address;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.domain.valueobject.CPF;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateClientUseCase createClientUseCase;

    @MockitoBean
    private FindClientUseCase findClientUseCase;

    @MockitoBean
    private UpdateClientUseCase updateClientUseCase;

    @MockitoBean
    private DeleteClientUseCase deleteClientUseCase;

    @MockitoBean
    private CreateAddressUseCase createAddressUseCase;

    @MockitoBean
    private UpdateAddressUseCase updateAddressUseCase;

    @MockitoBean
    private DeleteAddressUseCase deleteAddressUseCase;

    @MockitoBean
    private org.modelmapper.ModelMapper modelMapper;

    private ClientDTO clientDTO;
    private Client client;
    private AddressDTO addressDTO;
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
        List<AddressDTO> addressDTOs = new ArrayList<>();
        addressDTOs.add(addressDTO);

        clientDTO = new ClientDTO();
        clientDTO.setClientId(1L);
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setCpf("80080933076");
        clientDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        clientDTO.setAddresses(addressDTOs);

        // Setup Client and Address domain objects
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setId(1L);
        address.setStreet("Rua Principal");
        address.setNumber("123");
        address.setZipcode("01234567");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
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
    void shouldCreateClientSuccessfully() throws Exception {
        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(createClientUseCase.createClient(any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.cpf").value("80080933076"))
                .andExpect(jsonPath("$.addresses[0].street").value("Rua Principal"));

        verify(modelMapper).map(any(ClientDTO.class), eq(Client.class));
        verify(createClientUseCase).createClient(any(Client.class));
        verify(modelMapper).map(any(Client.class), eq(ClientDTO.class));
    }

    @Test
    void shouldReturnConflictWhenClientWithCpfExists() throws Exception {
        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(createClientUseCase.createClient(any(Client.class))).thenThrow(new DuplicateResourceException("Já existe cliente cadastrado com esse CPF"));

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isConflict());

        verify(modelMapper).map(any(ClientDTO.class), eq(Client.class));
        verify(createClientUseCase).createClient(any(Client.class));
    }

    @Test
    void shouldListAllClients() throws Exception {
        List<Client> clients = Arrays.asList(client);
        List<ClientDTO> clientDTOs = Arrays.asList(clientDTO);

        when(findClientUseCase.findAll()).thenReturn(clients);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(findClientUseCase).findAll();
        verify(modelMapper).map(any(Client.class), eq(ClientDTO.class));
    }

    @Test
    void shouldFindClientById() throws Exception {
        Long clientId = 1L;

        when(findClientUseCase.findById(clientId)).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(findClientUseCase).findById(clientId);
        verify(modelMapper).map(any(Client.class), eq(ClientDTO.class));
    }

    @Test
    void shouldFindClientByDocument() throws Exception {
        String cpf = "80080933076";

        when(findClientUseCase.findByCpf(cpf)).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/document/{document}", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.cpf").value(cpf));

        verify(findClientUseCase).findByCpf(cpf);
        verify(modelMapper).map(any(Client.class), eq(ClientDTO.class));
    }

    @Test
    void shouldUpdateClient() throws Exception {
        Long clientId = 1L;

        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(client);
        when(updateClientUseCase.updateClient(eq(clientId), any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(modelMapper).map(any(ClientDTO.class), eq(Client.class));
        verify(updateClientUseCase).updateClient(eq(clientId), any(Client.class));
        verify(modelMapper).map(any(Client.class), eq(ClientDTO.class));
    }

    @Test
    void shouldDeleteClient() throws Exception {
        Long clientId = 1L;

        doNothing().when(deleteClientUseCase).deleteClient(clientId);

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNoContent());

        verify(deleteClientUseCase).deleteClient(clientId);
    }

    @Test
    void shouldAddAddressToClient() throws Exception {
        Long clientId = 1L;

        when(modelMapper.map(any(AddressDTO.class), eq(AddressEntity.class))).thenReturn(addressEntity);
        when(createAddressUseCase.createAddress(eq(clientId), any(AddressEntity.class))).thenReturn(addressEntity);
        when(modelMapper.map(any(AddressEntity.class), eq(AddressDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(post("/clients/{idClient}/addresses", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.street").value("Rua Principal"));

        verify(modelMapper).map(any(AddressDTO.class), eq(AddressEntity.class));
        verify(createAddressUseCase).createAddress(eq(clientId), any(AddressEntity.class));
        verify(modelMapper).map(any(AddressEntity.class), eq(AddressDTO.class));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        Long clientId = 1L;
        Long addressId = 1L;

        when(modelMapper.map(any(AddressDTO.class), eq(AddressEntity.class))).thenReturn(addressEntity);
        when(updateAddressUseCase.updateAddress(eq(clientId), eq(addressId), any(AddressEntity.class))).thenReturn(addressEntity);
        when(modelMapper.map(any(AddressEntity.class), eq(AddressDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(put("/clients/{idClient}/addresses/{addressId}", clientId, addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.street").value("Rua Principal"));

        verify(modelMapper).map(any(AddressDTO.class), eq(AddressEntity.class));
        verify(updateAddressUseCase).updateAddress(eq(clientId), eq(addressId), any(AddressEntity.class));
        verify(modelMapper).map(any(AddressEntity.class), eq(AddressDTO.class));
    }

    @Test
    void shouldDeleteAddress() throws Exception {
        Long clientId = 1L;
        Long addressId = 1L;

        doNothing().when(deleteAddressUseCase).deleteAddress(clientId, addressId);

        mockMvc.perform(delete("/clients/{idClient}/addresses/{addressId}", clientId, addressId))
                .andExpect(status().isNoContent());

        verify(deleteAddressUseCase).deleteAddress(clientId, addressId);
    }
}