package com.postech.auramsclient.adapters.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {

    @Test
    public void testClientDTOConstructor() {
        Long clientId = 1L;
        String firstName = "João";
        String lastName = "Silva";
        String cpf = "12345678901";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        List<AddressDTO> addresses = Arrays.asList(new AddressDTO());

        ClientDTO clientDTO = new ClientDTO(clientId, firstName, lastName, cpf, birthDate, addresses);

        assertEquals(clientId, clientDTO.getClientId());
        assertEquals(firstName, clientDTO.getFirstName());
        assertEquals(lastName, clientDTO.getLastName());
        assertEquals(cpf, clientDTO.getCpf());
        assertEquals(birthDate, clientDTO.getBirthDate());
        assertEquals(addresses, clientDTO.getAddresses());
    }
}