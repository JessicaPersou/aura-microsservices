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
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final FindClientUseCase findClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;
    private final ModelMapper modelMapper;

    public ClientController(CreateClientUseCase createClientUseCase, FindClientUseCase findClientUseCase,
                            UpdateClientUseCase updateClientUseCase, DeleteClientUseCase deleteClientUseCase,
                            CreateAddressUseCase createAddressUseCase, UpdateAddressUseCase updateAddressUseCase,
                            DeleteAddressUseCase deleteAddressUseCase, ModelMapper modelMapper) {
        this.createClientUseCase = createClientUseCase;
        this.findClientUseCase = findClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.createAddressUseCase = createAddressUseCase;
        this.updateAddressUseCase = updateAddressUseCase;
        this.deleteAddressUseCase = deleteAddressUseCase;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            Client client = modelMapper.map(clientDTO, Client.class);
            Client createdClient = createClientUseCase.createClient(client);
            ClientDTO createdClientDTO = modelMapper.map(createdClient, ClientDTO.class);
            return new ResponseEntity<>(createdClientDTO, HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> listAllClients() {
        List<Client> clients = findClientUseCase.findAll();
        List<ClientDTO> clientDTOs = clients.stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(clientDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id) {
        Client client = findClientUseCase.findById(id);
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<ClientDTO> findClientByDocument(@PathVariable String document) {
        Client client = findClientUseCase.findByCpf(document);
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        Client updatedClient = updateClientUseCase.updateClient(id, client);
        ClientDTO updatedClientDTO = modelMapper.map(updatedClient, ClientDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClientDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        deleteClientUseCase.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{idClient}/addresses")
    public ResponseEntity<AddressDTO> addAddressToClient(@PathVariable Long idClient, @RequestBody AddressDTO addressDTO) {
        AddressEntity addressEntity = modelMapper.map(addressDTO, AddressEntity.class);
        var createdAddress = createAddressUseCase.createAddress(idClient, addressEntity);
        var createdAddressDTO = modelMapper.map(createdAddress, AddressDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddressDTO);
    }


    @PutMapping("/{idClient}/addresses/{addressId}")
    public ResponseEntity<AddressDTO> editAddress(@PathVariable Long idClient, @PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressEntity addressEntity = modelMapper.map(addressDTO, AddressEntity.class);
        AddressEntity updatedAddress = updateAddressUseCase.updateAddress(idClient, addressId, addressEntity);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(updatedAddress, AddressDTO.class));
    }

    @DeleteMapping("/{idClient}/addresses/{addressId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long idClient, @PathVariable Long addressId) {
        deleteAddressUseCase.deleteAddress(idClient, addressId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}