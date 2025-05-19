package com.postech.auramsclient.application.address;

import com.postech.auramsclient.gateway.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteAddressUseCase {

    private final AddressRepository addressRepository;

    public DeleteAddressUseCase(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void deleteAddress(Long clientId, Long addressId) {
        addressRepository.deleteAddress(clientId, addressId);
    }
}