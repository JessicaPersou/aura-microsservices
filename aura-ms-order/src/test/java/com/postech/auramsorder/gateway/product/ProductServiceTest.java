package com.postech.auramsorder.gateway.product;

import com.postech.auramsorder.adapter.dto.ProductDTO;
import com.postech.auramsorder.config.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService.setProductServiceUrl("http://localhost:8004/api/v1/products/");
    }

    @Test
    void verifyProduct_ShouldReturnTrue_WhenProductExists() {
        String sku = "SKUPRODUCT1";
        when(restTemplate.getForEntity(productService.getProductServiceUrl() + sku, String.class))
                .thenReturn(new ResponseEntity<>("Product exists", HttpStatus.OK));

        boolean result = productService.verifyProduct(sku);

        assertTrue(result);
    }

    @Test
    void verifyProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        String sku = "SKUPRODUCT";
        when(restTemplate.getForEntity(productService.getProductServiceUrl() + sku, String.class))
                .thenThrow(new RuntimeException("Product not found"));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.verifyProduct(sku);
        });

        assertEquals("Product not found: " + sku, exception.getMessage());
    }

    @Test
    void getProductPrice_ShouldReturnTotalPrice_WhenProductsExist() {
        List<String> skus = Arrays.asList("SKUPRODUCT", "SKUPRODUCT1");
        List<ProductDTO> mockProducts = Arrays.asList(
                new ProductDTO(1L, "Product1", "SKUPRODUCT", BigDecimal.valueOf(2), "Description 1"),
                new ProductDTO(1L, "Product1", "SKUPRODUCT1", BigDecimal.valueOf(2), "Description 1")
        );

        when(restTemplate.exchange(eq(productService.getProductServiceUrl() + "list/sku"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(mockProducts, HttpStatus.OK));

        BigDecimal result = productService.getProductPrice(skus);

        assertEquals(BigDecimal.valueOf(4).setScale(2), result);
    }

    @Test
    void getProductPrice_ShouldThrowProductNotFoundException_WhenProductsDoNotExist() {
        List<String> skus = Arrays.asList("12345", "67890");

        when(restTemplate.exchange(eq(productService.getProductServiceUrl() + "list/sku"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductPrice(skus);
        });

        assertEquals("Product not found: " + skus, exception.getMessage());
    }

    @Test
    void calculatePriceAmount_ShouldReturnCorrectTotalPrice() {
        List<ProductDTO> mockProducts = Arrays.asList(
                new ProductDTO(1L, "Product1", "SKUPRODUCT", BigDecimal.valueOf(2), "Description 1"),
                new ProductDTO(1L, "Product1", "SKUPRODUCT1", BigDecimal.valueOf(2), "Description 1")
        );

        BigDecimal result = productService.calculatePriceAmount(mockProducts);

        assertEquals(BigDecimal.valueOf(4).setScale(2), result);
    }

    @Test
    void formatarValor_ShouldReturnFormattedValue() {
        BigDecimal valor = BigDecimal.valueOf(1234.56);

        String result = productService.formatarValor(valor);

        assertEquals("R$ 1.234,56", result);
    }
}