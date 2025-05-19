package com.postech.auramsorder.gateway;

import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    OrderEntity save(OrderEntity orderEntity);

    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findAll();

    List<OrderEntity> findByStatus(String status);

    void deleteById(Long id);
}
