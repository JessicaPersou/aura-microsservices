package com.postech.auramsclient.gateway.database.jpa.repository;

import com.postech.auramsclient.gateway.database.jpa.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}
