package com.postech.auramsclient.gateway;

import com.postech.auramsclient.config.exceptions.ResourceNotFoundException;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import com.postech.auramsclient.gateway.database.jpa.repository.AddressJpaRepository;
import com.postech.auramsclient.gateway.database.jpa.repository.ClientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressRepositoryImplTest {

    @Mock
    private AddressJpaRepository addressJpaRepository;

    @Mock
    private ClientJpaRepository clientJpaRepository;

    @InjectMocks
    private AddressRepositoryImpl addressRepository;

    private Long clientId;
    private Long addressId;
    private ClientEntity clientEntity;
    private AddressEntity addressEntity;
    private AddressEntity existingAddressEntity;

    @BeforeEach
    void setUp() {
        clientId = 1L;
        addressId = 1L;

        clientEntity = new ClientEntity();
        clientEntity.setId(clientId);

        addressEntity = new AddressEntity();
        addressEntity.setStreet("Rua Principal");
        addressEntity.setNumber("123");
        addressEntity.setZipcode("01234567");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");

        existingAddressEntity = new AddressEntity();
        existingAddressEntity.setId(addressId);
        existingAddressEntity.setStreet("Rua Existente");
        existingAddressEntity.setNumber("456");
        existingAddressEntity.setZipcode("98765432");
        existingAddressEntity.setNeighborhood("Jardins");
        existingAddressEntity.setCity("São Paulo");
        existingAddressEntity.setState("SP");
        existingAddressEntity.setClient(clientEntity);
    }

    @Test
    void shouldAddAddressSuccessfully() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        AddressEntity result = addressRepository.addAddress(clientId, addressEntity);

        assertNotNull(result);
        assertEquals(clientEntity, result.getClient());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).save(addressEntity);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundOnAddAddress() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.addAddress(clientId, addressEntity);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verifyNoInteractions(addressJpaRepository);
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.of(existingAddressEntity));
        when(addressJpaRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        AddressEntity result = addressRepository.updateAddress(clientId, addressId, addressEntity);

        assertNotNull(result);
        assertEquals(addressId, result.getId());
        assertEquals(clientEntity, result.getClient());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verify(addressJpaRepository).save(addressEntity);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundOnUpdateAddress() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.updateAddress(clientId, addressId, addressEntity);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verifyNoInteractions(addressJpaRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundOnUpdateAddress() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.updateAddress(clientId, addressId, addressEntity);
        });

        assertEquals("Endereço não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verifyNoMoreInteractions(addressJpaRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotBelongToClientOnUpdateAddress() {
        ClientEntity otherClientEntity = new ClientEntity();
        otherClientEntity.setId(2L);

        AddressEntity addressWithDifferentClient = new AddressEntity();
        addressWithDifferentClient.setId(addressId);
        addressWithDifferentClient.setClient(otherClientEntity);

        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.of(addressWithDifferentClient));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.updateAddress(clientId, addressId, addressEntity);
        });

        assertEquals("Endereço não pertence ao cliente informado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verifyNoMoreInteractions(addressJpaRepository);
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.of(existingAddressEntity));
        doNothing().when(addressJpaRepository).deleteById(addressId);

        addressRepository.deleteAddress(clientId, addressId);

        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verify(addressJpaRepository).deleteById(addressId);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundOnDeleteAddress() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.deleteAddress(clientId, addressId);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verifyNoInteractions(addressJpaRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundOnDeleteAddress() {
        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.deleteAddress(clientId, addressId);
        });

        assertEquals("Endereço não encontrado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verifyNoMoreInteractions(addressJpaRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotBelongToClientOnDeleteAddress() {
        ClientEntity otherClientEntity = new ClientEntity();
        otherClientEntity.setId(2L);

        AddressEntity addressWithDifferentClient = new AddressEntity();
        addressWithDifferentClient.setId(addressId);
        addressWithDifferentClient.setClient(otherClientEntity);

        when(clientJpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.of(addressWithDifferentClient));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            addressRepository.deleteAddress(clientId, addressId);
        });

        assertEquals("Endereço não pertence ao cliente informado", exception.getMessage());
        verify(clientJpaRepository).findById(clientId);
        verify(addressJpaRepository).findById(addressId);
        verifyNoMoreInteractions(addressJpaRepository);
    }}
