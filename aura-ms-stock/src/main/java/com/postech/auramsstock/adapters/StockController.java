package com.postech.auramsstock.adapters;

import com.postech.auramsstock.adapters.dto.ReserveStockDTO;
import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.application.DeleteStockUseCase;
import com.postech.auramsstock.application.FindStockUseCase;
import com.postech.auramsstock.application.ReserveStockUseCase;
import com.postech.auramsstock.application.UpdateStockUseCase;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.domain.Stock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/stocks")
public class StockController {

    private final ReserveStockUseCase reserveStockUseCase;
    private final FindStockUseCase findStockUseCase;
    private final UpdateStockUseCase updateStock;
    private final DeleteStockUseCase deleteStockUseCase;
    private final ModelMapper modelMapper;

    public StockController(ReserveStockUseCase reserveStockUseCase, FindStockUseCase findStockUseCase,
                           UpdateStockUseCase updateStock, DeleteStockUseCase deleteStockUseCase, ModelMapper modelMapper) {
        this.reserveStockUseCase = reserveStockUseCase;
        this.findStockUseCase = findStockUseCase;
        this.updateStock = updateStock;
        this.deleteStockUseCase = deleteStockUseCase;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        Optional<StockEntity> stock = findStockUseCase.findById(id);
        StockDTO stockDTO = modelMapper.map(stock, StockDTO.class);
        return ResponseEntity.ok(stockDTO);
    }

    @PostMapping("/new-reserve")
    public ResponseEntity<Boolean> reserveProcess(@RequestBody List<ReserveStockDTO> reserveStockDTO) {
        reserveStockUseCase.reserveStock(reserveStockDTO);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/return")
    public ResponseEntity<Boolean> stockReturn(@RequestBody ReserveStockDTO reserveStockDTO) {
        reserveStockUseCase.returnStock(reserveStockDTO);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteStock(@PathVariable Long id) {
        deleteStockUseCase.delete(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        Stock updatedStock = updateStock.updateStock(id, stockDTO);
        StockDTO updatedStockDTO = modelMapper.map(updatedStock, StockDTO.class);
        return ResponseEntity.ok(updatedStockDTO);
    }

}