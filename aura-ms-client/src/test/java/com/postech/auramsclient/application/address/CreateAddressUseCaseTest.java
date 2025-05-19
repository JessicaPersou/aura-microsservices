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
class CreateAddressUseCaseTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CreateAddressUseCase createAddressUseCase;

    private Long clientId;
    private AddressEntity addressEntity;
    private AddressEntity savedAddressEntity;

    @BeforeEach
    void setUp() {
        clientId = 1L;

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);

        addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua Principal");
        addressEntity.setNumber("123");
        addressEntity.setZipcode("01234567");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");

        savedAddressEntity = new AddressEntity();
        savedAddressEntity.setId(1L);
        savedAddressEntity.setStreet("Rua Principal");
        savedAddressEntity.setNumber("123");
        savedAddressEntity.setZipcode("01234567");
        savedAddressEntity.setNeighborhood("Centro");
        savedAddressEntity.setCity("São Paulo");
        savedAddressEntity.setState("SP");
        savedAddressEntity.setClient(clientEntity);
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        when(addressRepository.addAddress(clientId, addressEntity)).thenReturn(savedAddressEntity);

        AddressEntity result = createAddressUseCase.createAddress(clientId, addressEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rua Principal", result.getStreet());
        assertEquals("123", result.getNumber());
        assertEquals("01234567", result.getZipcode());
        assertEquals("Centro", result.getNeighborhood());
        assertEquals("São Paulo", result.getCity());
        assertEquals("SP", result.getState());
        assertEquals(clientId, result.getClient().getId());

        verify(addressRepository).addAddress(clientId, addressEntity);
    }
}