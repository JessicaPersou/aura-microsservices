package com.postech.auramsstock.database;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.database.jpa.repository.StockJpaRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockRepositoryImplTest {

    @Mock
    private StockJpaRepository stockJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockRepositoryImpl stockRepository;

    private StockEntity stockEntity;

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
        stockEntity.setStatus(StatusEnum.AVALIABLE);
    }

    @Test
    @DisplayName("Should save stock entity successfully")
    void saveStockEntity() {
        when(stockJpaRepository.save(any(StockEntity.class))).thenReturn(stockEntity);

        StockEntity result = stockRepository.save(stockEntity);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("SMGX20-BLK", result.getSkuProduct());
        verify(stockJpaRepository, times(1)).save(stockEntity);
    }

    @Test
    @DisplayName("Should find stock by SKU successfully")
    void findBySkuProductSuccessful() {
        when(stockJpaRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));

        Optional<StockEntity> result = stockRepository.findBySkuProduct("SMGX20-BLK");

        assertTrue(result.isPresent());
        assertEquals("SMGX20-BLK", result.get().getSkuProduct());
        verify(stockJpaRepository, times(1)).findBySkuProduct("SMGX20-BLK");
    }

    @Test
    @DisplayName("Should throw exception when SKU is null or empty")
    void findBySkuProductWithNullSku() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockRepository.findBySkuProduct(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            stockRepository.findBySkuProduct("");
        });

        verify(stockJpaRepository, never()).findBySkuProduct(anyString());
    }

    @Test
    @DisplayName("Should find stock by ID successfully")
    void findByIdSuccessful() {
        when(stockJpaRepository.findById(anyLong())).thenReturn(Optional.of(stockEntity));

        Optional<StockEntity> result = stockRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(stockJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void findByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockRepository.findById(null);
        });

        verify(stockJpaRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Should update stock successfully")
    void updateStockSuccessful() {
        StockEntity updatedEntity = new StockEntity();
        updatedEntity.setId(1);
        updatedEntity.setSkuProduct("SMGX20-BLK");
        updatedEntity.setNameProduct("Smartphone Galaxy X20 Updated");
        updatedEntity.setQuantity(40L);
        updatedEntity.setValueUnit(new BigDecimal("899.99"));
        updatedEntity.setValueSale(new BigDecimal("999.99"));
        updatedEntity.setStatus(StatusEnum.AVALIABLE);

        when(stockJpaRepository.findById(anyLong())).thenReturn(Optional.of(stockEntity));
        when(stockJpaRepository.save(any(StockEntity.class))).thenReturn(updatedEntity);

        StockEntity result = stockRepository.updateStock(1L, updatedEntity);

        assertNotNull(result);
        assertEquals("Smartphone Galaxy X20 Updated", result.getNameProduct());
        assertEquals(40L, result.getQuantity());
        assertEquals(new BigDecimal("899.99"), result.getValueUnit());
        assertEquals(new BigDecimal("999.99"), result.getValueSale());

        verify(stockJpaRepository, times(1)).findById(1L);
        verify(stockJpaRepository, times(1)).save(any(StockEntity.class));
    }
}