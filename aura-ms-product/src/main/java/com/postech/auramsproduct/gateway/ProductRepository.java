package com.postech.auramsproduct.gateway;

import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.gateway.database.jpa.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    List<ProductEntity> findAllProductsBySku(List<String> sku);

    List<Product> findAll();

    void delete(Long id);

    boolean existsById(Long id);

    boolean existsBySku(String sku);
}