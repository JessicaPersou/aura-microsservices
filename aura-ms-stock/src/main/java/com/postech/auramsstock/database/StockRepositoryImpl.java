package com.postech.auramsstock.database;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.database.jpa.repository.StockJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;
    private final ModelMapper modelMapper;

    public StockRepositoryImpl(StockJpaRepository stockJpaRepository, ModelMapper modelMapper) {
        this.stockJpaRepository = stockJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StockEntity save(StockEntity stockEntity) {
        return stockJpaRepository.save(stockEntity);
    }

    @Override
    public Optional<StockEntity> findBySkuProduct(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU Não pode ser vazio");
        }
        return stockJpaRepository.findBySkuProduct(sku);
    }

    @Override
    public Optional<StockEntity> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return stockJpaRepository.findById(id.longValue());
    }

    @Override
    public StockEntity updateStock(Long id, StockEntity stockEntity) {
        if (id == null || stockEntity == null) {
            throw new IllegalArgumentException("ID e StockEntity não podem ser nulos");
        }

        Optional<StockEntity> existingStock = stockJpaRepository.findById(id);
        if (existingStock.isEmpty()) {
            throw new IllegalArgumentException("Estoque com o ID fornecido não encontrado");
        }

        StockEntity updatedStock = existingStock.get();
        updatedStock.setQuantity(stockEntity.getQuantity());
        updatedStock.setValueUnit(stockEntity.getValueUnit());
        updatedStock.setSkuProduct(stockEntity.getSkuProduct());
        updatedStock.setNameProduct(stockEntity.getNameProduct());
        updatedStock.setValueSale(stockEntity.getValueSale());

        return stockJpaRepository.save(updatedStock);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        stockJpaRepository.deleteById(id);
    }

    @Override
    public List<StockEntity> findAllBySku(List<String> skuProducts) {
        if (skuProducts == null || skuProducts.isEmpty()) {
            throw new IllegalArgumentException("Lista de SKU não pode ser vazia");
        }
        return stockJpaRepository.findAllBySkuProductIn(skuProducts);
    }

    @Override
    public boolean reserveBySkuAndQuantity(String sku, int quantity) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU não pode ser vazio");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        Optional<StockEntity> stockOptional = stockJpaRepository.findBySkuProduct(sku);
        if (stockOptional.isPresent()) {
            StockEntity stock = stockOptional.get();
            if (stock.getQuantity() >= quantity) {
                stock.setQuantity(stock.getQuantity() - quantity);
                stockJpaRepository.save(stock);
                return true;
            } else {
                throw new IllegalArgumentException("Quantidade solicitada maior que a disponível");
            }
        }
        return false;
    }


}
