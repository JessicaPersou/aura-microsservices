package com.postech.auramsclient.application;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.gateway.ClientRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UpdateClientUseCase {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public UpdateClientUseCase(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public Client updateClient(Long id, Client updatedClient) {
        ClientEntity existingClientEntity = clientRepository.findById(id);
        if (existingClientEntity == null) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        existingClientEntity.setFirstName(updatedClient.getFirstName());
        existingClientEntity.setLastName(updatedClient.getLastName());
        existingClientEntity.setBirthDate(updatedClient.getBirthDate());

        ClientEntity savedClientEntity = clientRepository.updateClient(id, existingClientEntity);
        return modelMapper.map(savedClientEntity, Client.class);
    }
}