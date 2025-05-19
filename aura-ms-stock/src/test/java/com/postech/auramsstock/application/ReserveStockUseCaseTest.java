package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.ReserveStockDTO;
import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReserveStockUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ReserveStockUseCase reserveStockUseCase;

    private StockEntity stockEntity;
    private ReserveStockDTO reserveStockDTO;

    @BeforeEach
    void setUp() {
        stockEntity = new StockEntity();
        stockEntity.setId(1);
        stockEntity.setSkuProduct("SKU123");
        stockEntity.setQuantity(10L);

        reserveStockDTO = new ReserveStockDTO("SKU123", 5L);
    }

    @Test
    @DisplayName("Deve verificar se o estoque está disponível para reserva")
    void checkStockReserve() {
        when(stockRepository.findBySkuProduct("SKU123")).thenReturn(Optional.of(stockEntity));

        boolean result = reserveStockUseCase.checkStockReserve("SKU123");

        assertTrue(result);
        verify(stockRepository, times(1)).findBySkuProduct("SKU123");
    }

    @Test
    @DisplayName("Deve reservar o estoque com sucesso")
    void reserveStockSuccess() {
        when(stockRepository.reserveBySkuAndQuantity(anyString(), anyInt())).thenReturn(true);

        List<ReserveStockDTO> result = reserveStockUseCase.reserveStock(List.of(reserveStockDTO));

        assertEquals(1, result.size());
        assertEquals("SKU123", result.get(0).getSku());
        verify(stockRepository, times(1)).reserveBySkuAndQuantity("SKU123", 5);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar reservar estoque insuficiente")
    void reserveStockInsufficient() {
        when(stockRepository.reserveBySkuAndQuantity(anyString(), anyInt())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                reserveStockUseCase.reserveStock(List.of(reserveStockDTO)));

        assertEquals("Falha ao reservar o estoque para o SKU: SKU123", exception.getMessage());
        verify(stockRepository, times(1)).reserveBySkuAndQuantity("SKU123", 5);
    }

    @Test
    @DisplayName("Deve retornar o estoque com sucesso")
    void returnStockSuccess() {
        when(stockRepository.findBySkuProduct("SKU123")).thenReturn(Optional.of(stockEntity));

        reserveStockUseCase.returnStock(reserveStockDTO);

        verify(stockRepository, times(1)).findBySkuProduct("SKU123");
        verify(stockRepository, times(1)).save(any(StockEntity.class));
        assertEquals(15L, stockEntity.getQuantity());
    }

    @Test
    @DisplayName("Deve criar novo estoque ao retornar produto inexistente")
    void returnStockNewProduct() {
        when(stockRepository.findBySkuProduct("SKU123")).thenReturn(Optional.empty());

        reserveStockUseCase.returnStock(reserveStockDTO);

        verify(stockRepository, times(1)).findBySkuProduct("SKU123");
        verify(stockRepository, times(1)).save(any(StockEntity.class));
    }
}