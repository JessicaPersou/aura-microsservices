package com.postech.auramsstock.database;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository {
    StockEntity save(StockEntity stockEntity);

    Optional<StockEntity> findBySkuProduct(String stockSkuProduct);

    Optional<StockEntity> findById(Long id);

    StockEntity updateStock(Long id, StockEntity stockEntity);

    void deleteById(Long id);

    List<StockEntity> findAllBySku(List<String> sku);

    boolean reserveBySkuAndQuantity(String sku, int quantity);
}
