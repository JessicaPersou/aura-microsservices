package com.postech.auramsclient.application.address;

import com.postech.auramsclient.gateway.AddressRepository;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private UpdateAddressUseCase updateAddressUseCase;

    private Long clientId;
    private Long addressId;
    private AddressEntity addressEntity;
    private AddressEntity updatedAddressEntity;

    @BeforeEach
    void setUp() {
        clientId = 1L;
        addressId = 1L;

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);

        addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua Atualizada");
        addressEntity.setNumber("456");
        addressEntity.setZipcode("98765432");
        addressEntity.setNeighborhood("Jardins");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");

        updatedAddressEntity = new AddressEntity();
        updatedAddressEntity.setId(addressId);
        updatedAddressEntity.setStreet("Rua Atualizada");
        updatedAddressEntity.setNumber("456");
        updatedAddressEntity.setZipcode("98765432");
        updatedAddressEntity.setNeighborhood("Jardins");
        updatedAddressEntity.setCity("São Paulo");
        updatedAddressEntity.setState("SP");
        updatedAddressEntity.setClient(clientEntity);
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        when(addressRepository.updateAddress(clientId, addressId, addressEntity)).thenReturn(updatedAddressEntity);

        AddressEntity result = updateAddressUseCase.updateAddress(clientId, addressId, addressEntity);

        assertNotNull(result);
        assertEquals(addressId, result.getId());
        assertEquals("Rua Atualizada", result.getStreet());
        assertEquals("456", result.getNumber());
        assertEquals("98765432", result.getZipcode());
        assertEquals("Jardins", result.getNeighborhood());
        assertEquals("São Paulo", result.getCity());
        assertEquals("SP", result.getState());
        assertEquals(clientId, result.getClient().getId());

        verify(addressRepository).updateAddress(clientId, addressId, addressEntity);
    }
}