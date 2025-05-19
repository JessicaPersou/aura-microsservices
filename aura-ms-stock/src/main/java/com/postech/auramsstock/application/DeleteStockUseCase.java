package com.postech.auramsstock.application;

import com.postech.auramsstock.database.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteStockUseCase {

    private final StockRepository stockRepository;

    public DeleteStockUseCase(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void delete(Long id) {
        stockRepository.deleteById(id);
    }
}