package com.postech.auramsclient.gateway;

import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository {
    boolean existsByCpf(String cpf);

    ClientEntity save(ClientEntity client);

    List<ClientEntity> findAll();

    ClientEntity findById(Long id);

    ClientEntity findByCpf(String cpf);

    ClientEntity updateClient(Long id, ClientEntity clientEntity);

    void deleteById(Long id);
}
