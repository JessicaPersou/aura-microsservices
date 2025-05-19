package com.postech.auramsproduct.gateway;


import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.gateway.database.jpa.entity.ProductEntity;
import com.postech.auramsproduct.gateway.database.jpa.repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        // Configurando objetos para usar nos testes
        product = new Product(1L, "Produto Teste", "SKU-123", new BigDecimal("99.99"), "Descrição do produto");

        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Produto Teste");
        productEntity.setSku("SKU-123");
        productEntity.setPrice(new BigDecimal("99.99"));
        productEntity.setDescription("Descrição do produto");
    }

    @Test
    @DisplayName("Deve salvar um produto com sucesso")
    void shouldSaveProductSuccessfully() {
        // Arrange
        when(modelMapper.map(any(Product.class), eq(ProductEntity.class))).thenReturn(productEntity);
        when(productJpaRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(modelMapper.map(any(ProductEntity.class), eq(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getSku(), savedProduct.getSku());

        verify(modelMapper).map(product, ProductEntity.class);
        verify(productJpaRepository).save(productEntity);
        verify(modelMapper).map(productEntity, Product.class);
    }

    @Test
    @DisplayName("Deve encontrar um produto pelo SKU com sucesso")
    void shouldFindProductBySkuSuccessfully() {
        // Arrange
        String sku = "SKU-123";
        when(productJpaRepository.findBySku(sku)).thenReturn(Optional.of(productEntity));
        when(modelMapper.map(any(ProductEntity.class), eq(Product.class))).thenReturn(product);

        // Act
        Optional<Product> foundProduct = productRepository.findBySku(sku);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getId(), foundProduct.get().getId());
        assertEquals(product.getSku(), foundProduct.get().getSku());

        verify(productJpaRepository).findBySku(sku);
        verify(modelMapper).map(productEntity, Product.class);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando não encontrar produto pelo SKU")
    void shouldReturnEmptyOptionalWhenProductNotFoundBySku() {
        // Arrange
        String sku = "NONEXISTENT-SKU";
        when(productJpaRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productRepository.findBySku(sku);

        // Assert
        assertFalse(foundProduct.isPresent());

        verify(productJpaRepository).findBySku(sku);
        verifyNoMoreInteractions(modelMapper); // Não deve chamar o modelMapper quando não encontrar
    }

    @Test
    @DisplayName("Deve encontrar um produto pelo ID com sucesso")
    void shouldFindProductByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        when(productJpaRepository.findById(id)).thenReturn(Optional.of(productEntity));
        when(modelMapper.map(any(ProductEntity.class), eq(Product.class))).thenReturn(product);

        // Act
        Optional<Product> foundProduct = productRepository.findById(id);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getId(), foundProduct.get().getId());

        verify(productJpaRepository).findById(id);
        verify(modelMapper).map(productEntity, Product.class);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando não encontrar produto pelo ID")
    void shouldReturnEmptyOptionalWhenProductNotFoundById() {
        // Arrange
        Long id = 999L;
        when(productJpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productRepository.findById(id);

        // Assert
        assertFalse(foundProduct.isPresent());

        verify(productJpaRepository).findById(id);
        verifyNoMoreInteractions(modelMapper);
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void shouldFindAllProductsSuccessfully() {
        // Arrange
        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Outro Produto");
        productEntity2.setSku("SKU-456");

        List<ProductEntity> productEntities = Arrays.asList(productEntity, productEntity2);

        Product product2 = new Product(2L, "Outro Produto", "SKU-456", new BigDecimal("199.99"), "Outra descrição");

        when(productJpaRepository.findAll()).thenReturn(productEntities);
        when(modelMapper.map(productEntity, Product.class)).thenReturn(product);
        when(modelMapper.map(productEntity2, Product.class)).thenReturn(product2);

        // Act
        List<Product> products = productRepository.findAll();

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(product.getId(), products.get(0).getId());
        assertEquals(product2.getId(), products.get(1).getId());

        verify(productJpaRepository).findAll();
        verify(modelMapper, times(2)).map(any(ProductEntity.class), eq(Product.class));
    }

    @Test
    @DisplayName("Deve deletar um produto pelo ID com sucesso")
    void shouldDeleteProductByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        doNothing().when(productJpaRepository).deleteById(id);

        // Act
        productRepository.delete(id);

        // Assert
        verify(productJpaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve verificar se existe produto pelo ID com sucesso")
    void shouldCheckIfProductExistsByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        when(productJpaRepository.existsById(id)).thenReturn(true);

        // Act
        boolean exists = productRepository.existsById(id);

        // Assert
        assertTrue(exists);
        verify(productJpaRepository).existsById(id);
    }

    @Test
    @DisplayName("Deve verificar se existe produto pelo SKU com sucesso")
    void shouldCheckIfProductExistsBySkuSuccessfully() {
        // Arrange
        String sku = "SKU-123";
        when(productJpaRepository.existsBySku(sku)).thenReturn(true);

        // Act
        boolean exists = productRepository.existsBySku(sku);

        // Assert
        assertTrue(exists);
        verify(productJpaRepository).existsBySku(sku);
    }
}