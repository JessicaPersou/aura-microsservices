package com.postech.auramsorder.adapter.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ProductDTO product = new ProductDTO();
        Long id = 1L;
        String name = "Product Name";
        String sku = "SKU123";
        BigDecimal price = BigDecimal.valueOf(99.99);
        String description = "Product Description";

        // Act
        product.setId(id);
        product.setName(name);
        product.setSku(sku);
        product.setPrice(price);
        product.setDescription(description);

        // Assert
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(sku, product.getSku());
        assertEquals(price, product.getPrice());
        assertEquals(description, product.getDescription());
    }

    @Test
    void testConstructorWithParameters() {
        // Arrange
        Long id = 1L;
        String name = "Product Name";
        String sku = "SKU123";
        BigDecimal price = BigDecimal.valueOf(99.99);
        String description = "Product Description";

        // Act
        ProductDTO product = new ProductDTO(id, name, sku, price, description);

        // Assert
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(sku, product.getSku());
        assertEquals(price, product.getPrice());
        assertEquals(description, product.getDescription());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        ProductDTO product = new ProductDTO();

        // Assert
        assertNotNull(product);
    }
}