package com.postech.auramsclient.application.address;

import com.postech.auramsclient.gateway.AddressRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateAddressUseCase {

    private final AddressRepository addressRepository;

    public UpdateAddressUseCase(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressEntity updateAddress(Long clientId, Long addressId, AddressEntity address) {
        return addressRepository.updateAddress(clientId, addressId, address);
    }
}