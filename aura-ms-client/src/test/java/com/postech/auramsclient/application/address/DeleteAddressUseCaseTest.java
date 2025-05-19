package com.postech.auramsclient.application.address;

import com.postech.auramsclient.gateway.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private DeleteAddressUseCase deleteAddressUseCase;

    @Test
    void shouldDeleteAddressSuccessfully() {
        Long clientId = 1L;
        Long addressId = 1L;

        doNothing().when(addressRepository).deleteAddress(clientId, addressId);

        deleteAddressUseCase.deleteAddress(clientId, addressId);

        verify(addressRepository).deleteAddress(clientId, addressId);
    }
}