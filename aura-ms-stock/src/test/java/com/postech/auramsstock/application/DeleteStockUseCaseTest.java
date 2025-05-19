package com.postech.auramsstock.application;

import com.postech.auramsstock.database.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteStockUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private DeleteStockUseCase deleteStockUseCase;

    @Test
    @DisplayName("Should delete stock successfully")
    void deleteStock() {
        // Arrange
        Long stockId = 1L;
        doNothing().when(stockRepository).deleteById(anyLong());

        // Act
        deleteStockUseCase.delete(stockId);

        // Assert
        verify(stockRepository, times(1)).deleteById(stockId);
    }
}