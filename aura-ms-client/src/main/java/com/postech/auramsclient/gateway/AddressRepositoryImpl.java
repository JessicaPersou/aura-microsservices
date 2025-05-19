package com.postech.auramsclient.gateway;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import com.postech.auramsclient.gateway.database.jpa.repository.AddressJpaRepository;
import com.postech.auramsclient.gateway.database.jpa.repository.ClientJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepository addressJpaRepository;
    private final ClientJpaRepository clientJpaRepository;

    public AddressRepositoryImpl(AddressJpaRepository addressJpaRepository, ClientJpaRepository clientJpaRepository) {
        this.addressJpaRepository = addressJpaRepository;
        this.clientJpaRepository = clientJpaRepository;
    }

    @Override
    public AddressEntity addAddress(Long clientId, AddressEntity address) {
        ClientEntity client = findClientById(clientId);
        address.setClient(client);
        return addressJpaRepository.save(address);
    }

    @Override
    public AddressEntity updateAddress(Long clientId, Long addressId, AddressEntity address) {
        ClientEntity client = findClientById(clientId);
        AddressEntity existingAddress = findAddressById(addressId);

        validateAddressOwnership(existingAddress, client);

        address.setId(addressId);
        address.setClient(client);
        return addressJpaRepository.save(address);
    }

    @Override
    public void deleteAddress(Long clientId, Long addressId) {
        ClientEntity client = findClientById(clientId);
        AddressEntity address = findAddressById(addressId);

        validateAddressOwnership(address, client);

        addressJpaRepository.deleteById(addressId);
    }

    // Métodos auxiliares
    private ClientEntity findClientById(Long clientId) {
        return clientJpaRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private AddressEntity findAddressById(Long addressId) {
        return addressJpaRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
    }

    private void validateAddressOwnership(AddressEntity address, ClientEntity client) {
        if (!address.getClient().getId().equals(client.getId())) {
            throw new ResourceNotFoundException("Endereço não pertence ao cliente informado");
        }
    }
}
