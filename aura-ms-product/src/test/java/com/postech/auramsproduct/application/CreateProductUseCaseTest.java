package com.postech.auramsproduct.application;

import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.gateway.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Product Test");
        productDTO.setSku("TV-HD-ULTRA");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setDescription("Test Description");

        Product product = new Product(1L, "Product Test", "TV-HD-ULTRA", new BigDecimal("99.99"), "Test Description");
        Product savedProduct = new Product(1L, "Product Test", "TV-HD-ULTRA", new BigDecimal("99.99"), "Test Description");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct, Product.class)).thenReturn(savedProduct);

        // Act
        Product result = createProductUseCase.execute(productDTO);

        // Assert
        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        assertEquals(productDTO.getName(), result.getName());
        assertEquals(productDTO.getSku(), result.getSku());
        assertEquals(productDTO.getPrice(), result.getPrice());
        assertEquals(productDTO.getDescription(), result.getDescription());

        verify(productRepository).save(any(Product.class));
        verify(modelMapper).map(savedProduct, Product.class);
    }

    @Test
    void shouldThrowExceptionWhenSkuIsNull() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Product Test");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setDescription("Test Description");

        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.execute(productDTO));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku("TV-HD-ULTRA");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setDescription("Test Description");

        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.execute(productDTO));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku("TV-HD-ULTRA");
        productDTO.setName("Product Test");
        productDTO.setPrice(BigDecimal.ZERO);
        productDTO.setDescription("Test Description");

        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.execute(productDTO));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku("TV-HD-ULTRA");
        productDTO.setName("Product Test");
        productDTO.setPrice(new BigDecimal("99.99"));

        assertThrows(IllegalArgumentException.class, () -> createProductUseCase.execute(productDTO));
    }
}