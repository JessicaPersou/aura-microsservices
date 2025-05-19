package com.postech.auramsclient.gateway.database.jpa.repository;

import com.postech.auramsclient.gateway.database.jpa.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByCpf(String cpf);

    ClientEntity findByCpf(String cpf);
}
