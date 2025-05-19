package com.postech.auramsclient.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateAddressWithValidData() {
        Address address = new Address(
                "Rua Principal",
                "123",
                "01234567",
                "Centro",
                "São Paulo",
                "SP"
        );

        assertEquals("Rua Principal", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("01234567", address.getZipcode());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
    }

    @Test
    void shouldThrowExceptionWhenStreetIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    null,
                    "123",
                    "01234567",
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Rua não pode ser nula", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNumberIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    "Rua Principal",
                    null,
                    "01234567",
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Número não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenZipcodeIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    null,
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("CEP não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNeighborhoodIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    null,
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Bairro não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCityIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    "Centro",
                    null,
                    "SP"
            );
        });

        assertEquals("Cidade não pode ser nula", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStateIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    "Centro",
                    "São Paulo",
                    null
            );
        });

        assertEquals("Estado não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStreetIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "",
                    "123",
                    "01234567",
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Rua não pode ser vazia", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNumberIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "Rua Principal",
                    "",
                    "01234567",
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Número não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenZipcodeIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "",
                    "Centro",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("CEP não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNeighborhoodIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    "",
                    "São Paulo",
                    "SP"
            );
        });

        assertEquals("Bairro não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCityIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    "Centro",
                    "",
                    "SP"
            );
        });

        assertEquals("Cidade não pode ser vazia", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStateIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Address(
                    "Rua Principal",
                    "123",
                    "01234567",
                    "Centro",
                    "São Paulo",
                    ""
            );
        });

        assertEquals("Estado não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldSetAndGetId() {
        Address address = new Address(
                "Rua Principal",
                "123",
                "01234567",
                "Centro",
                "São Paulo",
                "SP"
        );
        Long id = 1L;

        address.setId(id);

        assertEquals(id, address.getId());
    }

    @Test
    void shouldSetAndGetProperties() {
        Address address = new Address();

        address.setStreet("Rua Principal");
        address.setNumber("123");
        address.setZipcode("01234567");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");

        assertEquals("Rua Principal", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("01234567", address.getZipcode());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
    }
}