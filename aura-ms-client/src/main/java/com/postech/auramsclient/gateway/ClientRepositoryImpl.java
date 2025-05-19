package com.postech.auramsclient.gateway;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import com.postech.auramsclient.gateway.database.jpa.repository.ClientJpaRepository;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientRepositoryImpl implements ClientRepository {

    public final ClientJpaRepository clientJpaRepository;
    public final ModelMapper modelMapper;

    public ClientRepositoryImpl(ClientJpaRepository clientJpaRepository, ModelMapper modelMapper) {
        this.clientJpaRepository = clientJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return clientJpaRepository.existsByCpf(cpf);
    }

    @Override
    public ClientEntity save(ClientEntity clientEntity) {
        if (clientEntity != null) {
            return clientJpaRepository.save(clientEntity);
        }
        return clientEntity;
    }

    @Override
    public List<ClientEntity> findAll() {
        List<ClientEntity> clients = clientJpaRepository.findAll();
        clients.forEach(client -> Hibernate.initialize(client.getAddresses()));
        return clients;
    }

    @Override
    public ClientEntity findById(Long id) {
        ClientEntity client = clientJpaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        Hibernate.initialize(client.getAddresses());
        return client;
    }

    @Override
    public ClientEntity findByCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new ResourceNotFoundException("CPF não pode ser nulo ou vazio");
        }
        ClientEntity client = clientJpaRepository.findByCpf(cpf);
        if (client == null) {
            throw new ResourceNotFoundException("Cliente não encontrado com o CPF: " + cpf);
        }
        Hibernate.initialize(client.getAddresses());
        return clientJpaRepository.findByCpf(cpf);
    }

    @Override
    public ClientEntity updateClient(Long id, ClientEntity clientEntity) {
        ClientEntity existingClient = clientJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));
        return clientJpaRepository.save(existingClient);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado");
        }
        clientJpaRepository.deleteById(id);
    }
}
