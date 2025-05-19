package com.postech.auramsclient.application.address;

import com.postech.auramsclient.gateway.AddressRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateAddressUseCase {

    private final AddressRepository addressRepository;

    public CreateAddressUseCase(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressEntity createAddress(Long clientId, AddressEntity address) {
        return addressRepository.addAddress(clientId, address);
    }
}