package com.postech.auramsproduct.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsproduct.adapters.dto.ProductDTO;
import com.postech.auramsproduct.gateway.database.jpa.entity.ProductEntity;
import com.postech.auramsproduct.gateway.database.jpa.repository.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private ProductEntity testProduct1;
    private ProductEntity testProduct2;

    @BeforeEach
    void setUp() {
        // Limpa o banco de dados antes de cada teste
        productJpaRepository.deleteAll();

        // Cria produtos para os testes
        testProduct1 = new ProductEntity();
        testProduct1.setName("Produto Teste 1");
        testProduct1.setSku("TEST-SKU-001");
        testProduct1.setPrice(new BigDecimal("99.99"));
        testProduct1.setDescription("Descrição do produto teste 1");

        testProduct2 = new ProductEntity();
        testProduct2.setName("Produto Teste 2");
        testProduct2.setSku("TEST-SKU-002");
        testProduct2.setPrice(new BigDecimal("199.99"));
        testProduct2.setDescription("Descrição do produto teste 2");

        // Salva os produtos no banco de dados
        testProduct1 = productJpaRepository.save(testProduct1);
        testProduct2 = productJpaRepository.save(testProduct2);
    }

    @AfterEach
    void tearDown() {
        // Limpa o banco de dados após cada teste
        productJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void shouldCreateProductSuccessfully() throws Exception {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Novo Produto");
        productDTO.setSku("NEW-SKU-001");
        productDTO.setPrice(new BigDecimal("149.99"));
        productDTO.setDescription("Descrição do novo produto");

        // Act & Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Novo Produto")))
                .andExpect(jsonPath("$.sku", is("NEW-SKU-001")))
                .andExpect(jsonPath("$.price", is(149.99)))
                .andExpect(jsonPath("$.description", is("Descrição do novo produto")));
    }

    @Test
    @DisplayName("Deve buscar um produto pelo SKU com sucesso")
    void shouldFindProductBySkuSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/products/{sku}", testProduct1.getSku()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testProduct1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testProduct1.getName())))
                .andExpect(jsonPath("$.sku", is(testProduct1.getSku())))
                .andExpect(jsonPath("$.price", is(testProduct1.getPrice().doubleValue())))
                .andExpect(jsonPath("$.description", is(testProduct1.getDescription())));
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void shouldListAllProductsSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testProduct1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(testProduct1.getName())))
                .andExpect(jsonPath("$[1].id", is(testProduct2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(testProduct2.getName())));
    }
}