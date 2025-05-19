package com.postech.auramsclient.config.modelmapper;

import com.postech.auramsclient.adapters.dto.ClientDTO;
import com.postech.auramsclient.domain.Address;
import com.postech.auramsclient.domain.Client;
import com.postech.auramsclient.domain.valueobject.CPF;
import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.createTypeMap(ClientDTO.class, Client.class)
                .addMappings(mapper -> mapper.skip(Client::setCpf))
                .setPostConverter(context -> {
                    ClientDTO source = context.getSource();
                    Client destination = context.getDestination();
                    if (source.getCpf() != null) {
                        destination.setCpf(new CPF(source.getCpf()));
                    }
                    return destination;
                });

        modelMapper.createTypeMap(Client.class, ClientDTO.class)
                .addMappings(mapper -> mapper.skip(ClientDTO::setCpf))
                .setPostConverter(context -> {
                    Client source = context.getSource();
                    ClientDTO destination = context.getDestination();
                    if (source.getCpf() != null) {
                        destination.setCpf(source.getCpf().getValue());
                    }
                    return destination;
                });

        modelMapper.createTypeMap(Client.class, ClientEntity.class)
                .setPostConverter(context -> {
                    Client source = context.getSource();
                    ClientEntity destination = context.getDestination();
                    if (source.getCpf() != null) {
                        destination.setCpf(source.getCpf().getValue());
                    }
                    return destination;
                });

        modelMapper.createTypeMap(ClientEntity.class, Client.class)
                .setPostConverter(context -> {
                    ClientEntity source = context.getSource();
                    Client destination = context.getDestination();
                    if (source.getCpf() != null) {
                        destination.setCpf(new CPF(source.getCpf()));
                    }
                    return destination;
                });

        modelMapper.createTypeMap(AddressEntity.class, Address.class)
                .addMappings(mapper -> {
                    mapper.map(AddressEntity::getStreet, Address::setStreet);
                    mapper.map(AddressEntity::getNumber, Address::setNumber);
                    mapper.map(AddressEntity::getZipcode, Address::setZipcode);
                    mapper.map(AddressEntity::getNeighborhood, Address::setNeighborhood);
                    mapper.map(AddressEntity::getCity, Address::setCity);
                    mapper.map(AddressEntity::getState, Address::setState);
                });

        modelMapper.createTypeMap(Address.class, AddressEntity.class)
                .addMappings(mapper -> {
                    mapper.map(Address::getStreet, AddressEntity::setStreet);
                    mapper.map(Address::getNumber, AddressEntity::setNumber);
                    mapper.map(Address::getZipcode, AddressEntity::setZipcode);
                    mapper.map(Address::getNeighborhood, AddressEntity::setNeighborhood);
                    mapper.map(Address::getCity, AddressEntity::setCity);
                    mapper.map(Address::getState, AddressEntity::setState);
                });

        return modelMapper;
    }
}
