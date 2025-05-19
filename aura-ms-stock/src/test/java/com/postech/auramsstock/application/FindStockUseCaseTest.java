package com.postech.auramsstock.application;


import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindStockUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private FindStockUseCase findStockUseCase;

    private StockEntity stockEntity;

    @BeforeEach
    void setUp() {
        stockEntity = new StockEntity();
        stockEntity.setId(any());
        stockEntity.setSkuProduct("SKU123");
        stockEntity.setQuantity(10L);
    }

    @Test
    @DisplayName("Deve retornar o estoque quando o ID for encontrado")
    void findByIdWhenStockExists() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stockEntity));

        Optional<StockEntity> result = findStockUseCase.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(stockEntity, result.get());
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o ID não for encontrado")
    void findByIdWhenStockDoesNotExist() {
        when(stockRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<StockEntity> result = findStockUseCase.findById(2L);

        assertFalse(result.isPresent());
        verify(stockRepository, times(1)).findById(2L);
    }
}