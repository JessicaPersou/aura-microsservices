package com.postech.auramsorder.adapter.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        ClientDTO client = new ClientDTO();

        // Assert
        assertNotNull(client);
        assertNull(client.getClientId());
        assertNull(client.getFirstName());
        assertNull(client.getLastName());
        assertNull(client.getCpf());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long expectedId = 1L;
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String expectedCpf = "123.456.789-00";

        // Act
        ClientDTO client = new ClientDTO(expectedId, expectedFirstName, expectedLastName, expectedCpf);

        // Assert
        assertNotNull(client);
        assertEquals(expectedId, client.getClientId());
        assertEquals(expectedFirstName, client.getFirstName());
        assertEquals(expectedLastName, client.getLastName());
        assertEquals(expectedCpf, client.getCpf());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        ClientDTO client = new ClientDTO();
        Long expectedId = 2L;
        String expectedFirstName = "Jane";
        String expectedLastName = "Smith";
        String expectedCpf = "987.654.321-00";

        // Act
        client.setClientId(expectedId);
        client.setFirstName(expectedFirstName);
        client.setLastName(expectedLastName);
        client.setCpf(expectedCpf);

        // Assert
        assertEquals(expectedId, client.getClientId());
        assertEquals(expectedFirstName, client.getFirstName());
        assertEquals(expectedLastName, client.getLastName());
        assertEquals(expectedCpf, client.getCpf());
    }

    @Test
    void testNullValues() {
        ClientDTO client = new ClientDTO(null, null, null, null);
        assertNull(client.getClientId());
        assertNull(client.getFirstName());
        assertNull(client.getLastName());
        assertNull(client.getCpf());
    }
}