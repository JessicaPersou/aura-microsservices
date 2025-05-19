package com.postech.auramsclient.gateway;

import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository {
    AddressEntity addAddress(Long idClient, AddressEntity address);

    AddressEntity updateAddress(Long idClient, Long idAddress, AddressEntity address);

    void deleteAddress(Long idClient, Long idAddress);
}
