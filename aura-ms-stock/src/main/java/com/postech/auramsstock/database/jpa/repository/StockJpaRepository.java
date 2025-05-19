package com.postech.auramsstock.database.jpa.repository;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findBySkuProduct(String skuProduct);

    List<StockEntity> findAllBySkuProductIn(List<String> listSkuProduct);

}
