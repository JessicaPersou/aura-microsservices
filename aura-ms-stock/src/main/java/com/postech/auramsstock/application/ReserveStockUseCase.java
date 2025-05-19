package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.ReserveStockDTO;
import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReserveStockUseCase {

    private final StockRepository stockRepository;

    public ReserveStockUseCase(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public boolean checkStockReserve(String skuProduct) {
        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(skuProduct);
        return stockOptional.isPresent() && stockOptional.get().getQuantity() > 0;
    }

    @Transactional
    public List<ReserveStockDTO> reserveStock(List<ReserveStockDTO> reserveStockDTOList) {
        List<ReserveStockDTO> reserveStockDTO = new ArrayList<>();
        for (ReserveStockDTO response : reserveStockDTOList) {
            boolean isReserved = stockRepository.reserveBySkuAndQuantity(response.getSku(), response.getQuantity().intValue());
            ReserveStockDTO responseStockDTO = new ReserveStockDTO();
            responseStockDTO.setSku(response.getSku());
            responseStockDTO.setQuantity(response.getQuantity());
            reserveStockDTO.add(responseStockDTO);
            if (!isReserved) {
                throw new IllegalArgumentException("Falha ao reservar o estoque para o SKU: " + response.getSku());
            }
        }
        return reserveStockDTO;
    }

    @Transactional
    public void returnStock(ReserveStockDTO stockReserveDTO) {
        log.info("Retornando ao estoque o SKU: {} e a quantidade: {}", stockReserveDTO.getSku(), stockReserveDTO.getQuantity());

        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(stockReserveDTO.getSku());

        if (stockOptional.isEmpty()) {
            log.warn("Estoque não encontrado SKU: {}, criando uma nova gravação", stockReserveDTO.getSku());
            StockEntity newStock = new StockEntity();
            newStock.setSkuProduct(stockReserveDTO.getSku());
            newStock.setQuantity(stockReserveDTO.getQuantity());
            stockRepository.save(newStock);
            return;
        }

        StockEntity stock = stockOptional.get();
        stock.setQuantity(stock.getQuantity() + stockReserveDTO.getQuantity());
        stockRepository.save(stock);

        log.info("Estoque retornado com sucesso, retornando a quantidade para: {}", stock.getQuantity());
    }

}
