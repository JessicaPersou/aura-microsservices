package com.postech.auramsclient.gateway.database.jpa.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientEntityTest {

    @Test
    void shouldCreateClientEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setFirstName("John");
        clientEntity.setLastName("Doe");
        clientEntity.setCpf("80080933076");
        clientEntity.setBirthDate(LocalDate.of(1990, 1, 1));

        List<AddressEntity> addresses = new ArrayList<>();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Rua Principal");
        addressEntity.setNumber("123");
        addressEntity.setZipcode("01234567");
        addressEntity.setNeighborhood("Centro");
        addressEntity.setCity("São Paulo");
        addressEntity.setState("SP");
        addressEntity.setClient(clientEntity);
        addresses.add(addressEntity);

        clientEntity.setAddresses(addresses);

        assertEquals(1L, clientEntity.getId());
        assertEquals("John", clientEntity.getFirstName());
        assertEquals("Doe", clientEntity.getLastName());
        assertEquals("80080933076", clientEntity.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), clientEntity.getBirthDate());
        assertNotNull(clientEntity.getAddresses());
        assertEquals(1, clientEntity.getAddresses().size());
    }

    @Test
    void shouldCreateClientEntityWithBuilder() {
        ClientEntity clientEntity = ClientEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .cpf("80080933076")
                .birthDate(LocalDate.of(1990, 1, 1))
                .addresses(new ArrayList<>())
                .build();

        assertEquals(1L, clientEntity.getId());
        assertEquals("John", clientEntity.getFirstName());
        assertEquals("Doe", clientEntity.getLastName());
        assertEquals("80080933076", clientEntity.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), clientEntity.getBirthDate());
        assertNotNull(clientEntity.getAddresses());
    }

    @Test
    void shouldCreateClientEntityWithNoArgsConstructor() {
        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setId(1L);
        assertEquals(1L, clientEntity.getId());

        clientEntity.setFirstName("John");
        assertEquals("John", clientEntity.getFirstName());

        clientEntity.setLastName("Doe");
        assertEquals("Doe", clientEntity.getLastName());

        clientEntity.setCpf("80080933076");
        assertEquals("80080933076", clientEntity.getCpf());

        clientEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        assertEquals(LocalDate.of(1990, 1, 1), clientEntity.getBirthDate());

        List<AddressEntity> addresses = new ArrayList<>();
        clientEntity.setAddresses(addresses);
        assertEquals(addresses, clientEntity.getAddresses());
    }

    @Test
    void shouldCreateClientEntityWithAllArgsConstructor() {
        List<AddressEntity> addresses = new ArrayList<>();

        ClientEntity clientEntity = new ClientEntity(
                1L,
                "John",
                "Doe",
                "80080933076",
                LocalDate.of(1990, 1, 1),
                addresses
        );

        assertEquals(1L, clientEntity.getId());
        assertEquals("John", clientEntity.getFirstName());
        assertEquals("Doe", clientEntity.getLastName());
        assertEquals("80080933076", clientEntity.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), clientEntity.getBirthDate());
        assertEquals(addresses, clientEntity.getAddresses());
    }
}