package com.postech.auramsclient.application;

import com.postech.auramsclient.config.exceptions.DuplicateResourceException;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.gateway.ClientRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public CreateClientUseCase(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public Client createClient(Client client) {
        String cpf = client.getCpf().getValue();
        if (clientRepository.existsByCpf(cpf)) {
            throw new DuplicateResourceException("Já existe cliente cadastrado com esse CPF");
        }
        ClientEntity clientEntity = modelMapper.map(client, ClientEntity.class);
        ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        return modelMapper.map(savedClientEntity, Client.class);
    }
}