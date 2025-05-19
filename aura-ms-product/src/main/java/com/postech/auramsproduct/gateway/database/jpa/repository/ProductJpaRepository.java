package com.postech.auramsproduct.gateway.database.jpa.repository;

import com.postech.auramsproduct.gateway.database.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySku(String sku);
    boolean existsBySku(String sku);
    List<ProductEntity> findAllProductsBySkuIn(List<String> sku);
}
