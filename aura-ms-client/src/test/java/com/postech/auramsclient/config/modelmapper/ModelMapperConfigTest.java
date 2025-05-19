package com.postech.auramsclient.config.modelmapper;

import com.postech.auramsclient.adapters.dto.ClientDTO;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.domain.valueobject.CPF;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperConfigTest {

    @Test
    void shouldMapClientDTOToClient() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        ClientDTO dto = new ClientDTO();
        dto.setCpf("80080933076");

        Client client = modelMapper.map(dto, Client.class);

        assertEquals("80080933076", client.getCpf().getValue());
    }

    @Test
    void shouldMapClientToClientDTO() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        Client client = new Client();
        client.setCpf(new CPF("80080933076"));

        ClientDTO dto = modelMapper.map(client, ClientDTO.class);

        assertEquals("80080933076", dto.getCpf());
    }

    @Test
    void shouldMapClientToClientEntity() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        Client client = new Client();
        client.setCpf(new CPF("80080933076"));

        ClientEntity entity = modelMapper.map(client, ClientEntity.class);

        assertEquals("80080933076", entity.getCpf());
    }

    @Test
    void shouldMapClientEntityToClient() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        ClientEntity entity = new ClientEntity();
        entity.setCpf("80080933076");

        Client client = modelMapper.map(entity, Client.class);

        assertEquals("80080933076", client.getCpf().getValue());
    }

    @Test
    void shouldHandleNullCPFWhenMapping() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();

        ClientDTO dto = new ClientDTO();
        Client clientFromDTO = modelMapper.map(dto, Client.class);
        assertNull(clientFromDTO.getCpf());

        Client client = new Client();
        ClientDTO dtoFromClient = modelMapper.map(client, ClientDTO.class);
        assertNull(dtoFromClient.getCpf());

        ClientEntity entityFromClient = modelMapper.map(client, ClientEntity.class);
        assertNull(entityFromClient.getCpf());

        ClientEntity entity = new ClientEntity();
        Client clientFromEntity = modelMapper.map(entity, Client.class);
        assertNull(clientFromEntity.getCpf());
    }
}
