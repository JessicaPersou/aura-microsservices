package com.postech.auramsclient.application;

import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.gateway.ClientRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindClientUseCase {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public FindClientUseCase(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public List<Client> findAll() {
        List<ClientEntity> listClients = clientRepository.findAll();
        List<Client> clients = new ArrayList<>();
        for (ClientEntity clientEntity : listClients) {
            Client client = modelMapper.map(clientEntity, Client.class);
            clients.add(client);
        }
        return clients;
    }

    public Client findById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id);
        return modelMapper.map(clientEntity, Client.class);
    }

    public Client findByCpf(String cpf) {
        ClientEntity clientEntity = clientRepository.findByCpf(cpf);
        return modelMapper.map(clientEntity, Client.class);
    }
}
