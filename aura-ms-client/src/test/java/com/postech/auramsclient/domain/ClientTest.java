package com.postech.auramsclient.domain;

import com.postech.auramsclient.domain.valueobject.CPF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private String firstName;
    private String lastName;
    private CPF cpf;
    private LocalDate birthDate;
    private List<Address> addresses;
    private Address address;

    @BeforeEach
    void setUp() {
        firstName = "John";
        lastName = "Doe";
        cpf = new CPF("80080933076");
        birthDate = LocalDate.of(1990, 1, 1);

        address = new Address(
                "Rua Principal",
                "123",
                "01234567",
                "Centro",
                "São Paulo",
                "SP"
        );

        addresses = new ArrayList<>();
        addresses.add(address);
    }

    @Test
    void shouldCreateClientWithValidData() {
        Client client = new Client(firstName, lastName, cpf, birthDate, addresses);

        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(cpf, client.getCpf());
        assertEquals(birthDate, client.getBirthDate());
        assertEquals(addresses, client.getAddresses());
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Client(null, lastName, cpf, birthDate, addresses);
        });

        assertEquals("Nome não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Client(firstName, null, cpf, birthDate, addresses);
        });

        assertEquals("Sobrenome não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCPFIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Client(firstName, lastName, null, birthDate, addresses);
        });

        assertEquals("CPF não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Client(firstName, lastName, cpf, null, addresses);
        });

        assertEquals("Data de nascimento não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressesIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Client(firstName, lastName, cpf, birthDate, null);
        });

        assertEquals("Endereço não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("", lastName, cpf, birthDate, addresses);
        });

        assertEquals("Nome não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client(firstName, "", cpf, birthDate, addresses);
        });

        assertEquals("Sobrenome não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressesIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client(firstName, lastName, cpf, birthDate, new ArrayList<>());
        });

        assertEquals("Endereço não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenClientIsNotAdult() {
        LocalDate minorBirthDate = LocalDate.now().minusYears(17);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client(firstName, lastName, cpf, minorBirthDate, addresses);
        });

        assertEquals("Cliente deve ter81 anos, ou mais.", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenClientIsAdult() {
        Client client = new Client(firstName, lastName, cpf, birthDate, addresses);
        assertTrue(client.isAdult());
    }

    @Test
    void shouldAddAddressToClient() {
        Client client = new Client(firstName, lastName, cpf, birthDate, addresses);
        Address newAddress = new Address(
                "Rua Secundária",
                "456",
                "98765432",
                "Jardins",
                "São Paulo",
                "SP"
        );

        client.addAddress(newAddress);

        assertEquals(2, client.getAddresses().size());
        assertTrue(client.getAddresses().contains(newAddress));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullAddress() {
        Client client = new Client(firstName, lastName, cpf, birthDate, addresses);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            client.addAddress(null);
        });

        assertEquals("Endereço não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldSetAndGetId() {
        Client client = new Client(firstName, lastName, cpf, birthDate, addresses);
        Long id = 1L;

        client.setId(id);

        assertEquals(id, client.getId());
    }
}