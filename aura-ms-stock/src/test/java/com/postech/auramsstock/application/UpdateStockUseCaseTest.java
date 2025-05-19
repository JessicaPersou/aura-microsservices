package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.domain.Stock;
import com.postech.auramsstock.domain.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateStockUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateStockUseCase updateStockUseCase;

    private StockEntity stockEntity;
    private StockDTO stockDTO;
    private Stock stock;

    @BeforeEach
    void setUp() {
        stockEntity = new StockEntity();
        stockEntity.setId(1);
        stockEntity.setSkuProduct("SMGX20-BLK");
        stockEntity.setNameProduct("Smartphone Galaxy X20");
        stockEntity.setQuantity(50L);
        stockEntity.setValueUnit(new BigDecimal("999.99"));
        stockEntity.setValueSale(new BigDecimal("1099.99"));
        stockEntity.setTotalValue(new BigDecimal("49999.50"));
        stockEntity.setDtRegister(LocalDateTime.now());

        stockDTO = new StockDTO();
        stockDTO.setSkuProduct("SMGX20-BLK");
        stockDTO.setNameProduct("Smartphone Galaxy X20 Updated");
        stockDTO.setQuantity(40L);
        stockDTO.setValueUnit(new BigDecimal("899.99"));
        stockDTO.setValueSale(new BigDecimal("999.99"));
        stockDTO.setStatus(StatusEnum.AVALIABLE);

        stock = new Stock();
        stock.setId(1);
        stock.setSkuProduct("SMGX20-BLK");
        stock.setNameProduct("Smartphone Galaxy X20 Updated");
        stock.setQuantity(40L);
        stock.setValueUnit(new BigDecimal("899.99"));
        stock.setValueSale(new BigDecimal("999.99"));
        stock.setTotalValue(new BigDecimal("39999.60"));
        stock.setStatus(StatusEnum.AVALIABLE);
    }

    @Test
    @DisplayName("Should update stock successfully")
    void updateStockSuccessful() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stockEntity));
        when(stockRepository.updateStock(anyLong(), any(StockEntity.class))).thenReturn(stockEntity);
        when(modelMapper.map(any(), any())).thenReturn(stock);

        Stock result = updateStockUseCase.updateStock(1L, stockDTO);

        assertEquals("Smartphone Galaxy X20 Updated", result.getNameProduct());
        assertEquals(40L, result.getQuantity());
        assertEquals(new BigDecimal("899.99"), result.getValueUnit());
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void updateStockWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            updateStockUseCase.updateStock(null, stockDTO);
        });
    }

    @Test
    @DisplayName("Should throw exception when StockDTO is null")
    void updateStockWithNullDTO() {
        assertThrows(IllegalArgumentException.class, () -> {
            updateStockUseCase.updateStock(1L, null);
        });
    }

    @Test
    @DisplayName("Should throw exception when stock not found")
    void updateStockNotFound() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            updateStockUseCase.updateStock(999L, stockDTO);
        });
    }
}