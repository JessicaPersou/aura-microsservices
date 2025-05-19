package com.postech.auramsproduct.application;

import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.config.exception.ProductNotFoundException;
import com.postech.auramsproduct.domain.Product;
import com.postech.auramsproduct.gateway.ProductRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FindProductUseCase findProductUseCase;

    private Product product1;
    private Product product2;
    private ProductDTO productDTO1;
    private ProductDTO productDTO2;

    @BeforeEach
    void setUp() {
        // Configurando objetos de domínio para os testes
        product1 = new Product(1L, "Produto 1", "SKU-001", new BigDecimal("99.99"), "Descrição do produto 1");
        product2 = new Product(2L, "Produto 2", "SKU-002", new BigDecimal("199.99"), "Descrição do produto 2");

        // Configurando DTOs correspondentes
        productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        productDTO1.setName("Produto 1");
        productDTO1.setSku("SKU-001");
        productDTO1.setPrice(new BigDecimal("99.99"));
        productDTO1.setDescription("Descrição do produto 1");

        productDTO2 = new ProductDTO();
        productDTO2.setId(2L);
        productDTO2.setName("Produto 2");
        productDTO2.setSku("SKU-002");
        productDTO2.setPrice(new BigDecimal("199.99"));
        productDTO2.setDescription("Descrição do produto 2");
    }

    @Test
    @DisplayName("Deve encontrar um produto pelo SKU com sucesso")
    void shouldFindProductBySkuSuccessfully() {
        // Arrange
        String sku = "SKU-001";
        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product1));
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(productDTO1);

        // Act
        ProductDTO foundProduct = findProductUseCase.findBySku(sku);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(productDTO1.getId(), foundProduct.getId());
        assertEquals(productDTO1.getSku(), foundProduct.getSku());

        verify(productRepository).findBySku(sku);
        verify(modelMapper).map(product1, ProductDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado pelo SKU")
    void shouldThrowExceptionWhenProductNotFoundBySku() {
        // Arrange
        String sku = "NONEXISTENT-SKU";
        when(productRepository.findBySku(sku)).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> findProductUseCase.findBySku(sku)
        );

        assertTrue(exception.getMessage().contains(sku));
        verify(productRepository).findBySku(sku);
        verifyNoInteractions(modelMapper);
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void shouldFindAllProductsSuccessfully() {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(productDTO1);
        when(modelMapper.map(product2, ProductDTO.class)).thenReturn(productDTO2);

        // Act
        List<ProductDTO> foundProducts = findProductUseCase.findAll();

        // Assert
        assertNotNull(foundProducts);
        assertEquals(2, foundProducts.size());
        assertEquals(productDTO1.getId(), foundProducts.get(0).getId());
        assertEquals(productDTO2.getId(), foundProducts.get(1).getId());

        verify(productRepository).findAll();
        verify(modelMapper).map(product1, ProductDTO.class);
        verify(modelMapper).map(product2, ProductDTO.class);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver produtos")
    void shouldReturnEmptyListWhenNoProductsExist() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductDTO> foundProducts = findProductUseCase.findAll();

        // Assert
        assertNotNull(foundProducts);
        assertTrue(foundProducts.isEmpty());

        verify(productRepository).findAll();
        verifyNoInteractions(modelMapper);
    }

    @Test
    @DisplayName("Deve encontrar um produto pelo ID com sucesso")
    void shouldFindProductByIdSuccessfully() {
        // Arrange
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product1));
        when(modelMapper.map(product1, ProductDTO.class)).thenReturn(productDTO1);

        // Act
        ProductDTO foundProduct = findProductUseCase.findById(id);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(productDTO1.getId(), foundProduct.getId());

        verify(productRepository).findById(id);
        verify(modelMapper).map(product1, ProductDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado pelo ID")
    void shouldThrowExceptionWhenProductNotFoundById() {
        // Arrange
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> findProductUseCase.findById(id)
        );

        assertTrue(exception.getMessage().contains(id.toString()));
        verify(productRepository).findById(id);
        verifyNoInteractions(modelMapper);
    }
}