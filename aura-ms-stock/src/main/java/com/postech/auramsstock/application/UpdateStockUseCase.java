package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.domain.Stock;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UpdateStockUseCase {

    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    public UpdateStockUseCase(StockRepository stockRepository, ModelMapper modelMapper) {
        this.stockRepository = stockRepository;
        this.modelMapper = modelMapper;
    }

    public Stock updateStock(Long id, StockDTO stockDTO) {
        if (id == null || stockDTO == null) {
            throw new IllegalArgumentException("ID e DTO de estoque não podem ser nulos");
        }

        StockEntity stockEntity = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado para o ID fornecido"));

        stockEntity.setNameProduct(stockDTO.getNameProduct());
        stockEntity.setQuantity(stockDTO.getQuantity());
        stockEntity.setValueUnit(stockDTO.getValueUnit());
        stockEntity.setValueSale(stockDTO.getValueSale().multiply(BigDecimal.valueOf(10)));
        stockEntity.setTotalValue(stockDTO.getValueSale().multiply(BigDecimal.valueOf(stockDTO.getQuantity())));

        stockRepository.updateStock(id, stockEntity);

        return modelMapper.map(stockEntity, Stock.class);
    }
}
