package com.postech.auramsproduct.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void shouldCreateProductSuccessfully() {
        Long id = 1L;
        String name = "Produto Teste";
        String sku = "SKU123";
        BigDecimal price = new BigDecimal("99.99");
        String description = "Descrição do produto teste";

        Product product = new Product(id, name, sku, price, description);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(sku, product.getSku());
        assertEquals(price, product.getPrice());
        assertEquals(description, product.getDescription());
    }

    @Test
    void shouldCreateEmptyProductAndSetValuesSuccessfully() {
        Product product = new Product();
        Long id = 1L;
        String name = "Produto Teste";
        String sku = "SKU123";
        BigDecimal price = new BigDecimal("99.99");
        String description = "Descrição do produto teste";

        product.setId(id);
        product.setName(name);
        product.setSku(sku);
        product.setPrice(price);
        product.setDescription(description);

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(sku, product.getSku());
        assertEquals(price, product.getPrice());
        assertEquals(description, product.getDescription());
    }
}