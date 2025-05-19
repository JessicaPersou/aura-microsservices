package com.postech.auramsstock.application;

import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindStockUseCase {

    private final StockRepository stockRepository;

    public FindStockUseCase(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Optional<StockEntity> findById(Long id) {
        return stockRepository.findById(id);
    }

}