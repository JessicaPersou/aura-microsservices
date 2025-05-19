package com.postech.auramsclient.gateway.database.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressEntityTest {

    @Test
    void shouldCreateAddressEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua Principal");
        addressEntity.setNumber("123");
        addressEntity.setZipcode("01234567");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");
        addressEntity.setClient(clientEntity);

        assertEquals(1L, addressEntity.getId());
        assertEquals("Rua Principal", addressEntity.getStreet());
        assertEquals("123", addressEntity.getNumber());
        assertEquals("01234567", addressEntity.getZipcode());
        assertEquals("Centro", addressEntity.getNeighborhood());
        assertEquals("São Paulo", addressEntity.getCity());
        assertEquals("SP", addressEntity.getState());
        assertEquals(clientEntity, addressEntity.getClient());
    }

    @Test
    void shouldCreateAddressEntityWithBuilder() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);

        AddressEntity addressEntity = AddressEntity.builder()
                .id(1L)
                .street("Rua Principal")
                .number("123")
                .zipcode("01234567")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .client(clientEntity)
                .build();

        assertEquals(1L, addressEntity.getId());
        assertEquals("Rua Principal", addressEntity.getStreet());
        assertEquals("123", addressEntity.getNumber());
        assertEquals("01234567", addressEntity.getZipcode());
        assertEquals("Centro", addressEntity.getNeighborhood());
        assertEquals("São Paulo", addressEntity.getCity());
        assertEquals("SP", addressEntity.getState());
        assertEquals(clientEntity, addressEntity.getClient());
    }

    @Test
    void shouldCreateAddressEntityWithNoArgsConstructor() {
        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setId(1L);
        assertEquals(1L, addressEntity.getId());

        addressEntity.setStreet("Rua Principal");
        assertEquals("Rua Principal", addressEntity.getStreet());

        addressEntity.setNumber("123");
        assertEquals("123", addressEntity.getNumber());

        addressEntity.setZipcode("01234567");
        assertEquals("01234567", addressEntity.getZipcode());

        addressEntity.setNeighborhood("Centro");
        assertEquals("Centro", addressEntity.getNeighborhood());

        addressEntity.setCity("São Paulo");
        assertEquals("São Paulo", addressEntity.getCity());

        addressEntity.setState("SP");
        assertEquals("SP", addressEntity.getState());

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);

        addressEntity.setClient(clientEntity);
        assertEquals(clientEntity, addressEntity.getClient());
    }

    @Test
    void shouldCreateAddressEntityWithAllArgsConstructor() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);

        AddressEntity addressEntity = new AddressEntity(
                1L,
                "Rua Principal",
                "123",
                "01234567",
                "Centro",
                "São Paulo",
                "SP",
                clientEntity
        );

        assertEquals(1L, addressEntity.getId());
        assertEquals("Rua Principal", addressEntity.getStreet());
        assertEquals("123", addressEntity.getNumber());
        assertEquals("01234567", addressEntity.getZipcode());
        assertEquals("Centro", addressEntity.getNeighborhood());
        assertEquals("São Paulo", addressEntity.getCity());
        assertEquals("SP", addressEntity.getState());
        assertEquals(clientEntity, addressEntity.getClient());
    }
}