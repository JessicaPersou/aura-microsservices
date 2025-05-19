package com.postech.auramsorder.gateway.database.jpa.repository;

import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(String status);
}