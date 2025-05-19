package com.postech.auramsstock.domain;

import com.postech.auramsstock.domain.enums.StatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StockTest {

    @Test
    @DisplayName("Should create empty stock with default constructor")
    void createEmptyStock() {
        // Act
        Stock stock = new Stock();

        // Assert
        assertNull(stock.getId());
        assertNull(stock.getSkuProduct());
        assertNull(stock.getNameProduct());
        assertNull(stock.getQuantity());
        assertNull(stock.getTotalValue());
        assertNull(stock.getValueUnit());
        assertNull(stock.getValueSale());
        assertNull(stock.getStatus());
        assertNull(stock.getDtRegister());
    }

    @Test
    @DisplayName("Should create stock with all attributes using parameterized constructor")
    void createStockWithParameterizedConstructor() {
        // Arrange
        Integer id = 1;
        String skuProduct = "SMGX20-BLK";
        String nameProduct = "Smartphone Galaxy X20";
        Long quantity = 50L;
        BigDecimal totalValue = new BigDecimal("49999.50");
        BigDecimal valueUnit = new BigDecimal("999.99");
        BigDecimal valueSale = new BigDecimal("1099.99");
        StatusEnum status = StatusEnum.AVALIABLE;
        LocalDateTime dtRegister = LocalDateTime.now();

        // Act
        Stock stock = new Stock(id, skuProduct, nameProduct, quantity, totalValue,
                valueUnit, valueSale, status, dtRegister);

        // Assert
        assertEquals(id, stock.getId());
        assertEquals(skuProduct, stock.getSkuProduct());
        assertEquals(nameProduct, stock.getNameProduct());
        assertEquals(quantity, stock.getQuantity());
        assertEquals(totalValue, stock.getTotalValue());
        assertEquals(valueUnit, stock.getValueUnit());
        assertEquals(valueSale, stock.getValueSale());
        assertEquals(status, stock.getStatus());
        assertEquals(dtRegister, stock.getDtRegister());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void setAndGetId() {
        // Arrange
        Stock stock = new Stock();
        Integer id = 1;

        // Act
        stock.setId(id);

        // Assert
        assertEquals(id, stock.getId());
    }

    @Test
    @DisplayName("Should set and get skuProduct correctly")
    void setAndGetSkuProduct() {
        // Arrange
        Stock stock = new Stock();
        String skuProduct = "SMGX20-BLK";

        // Act
        stock.setSkuProduct(skuProduct);

        // Assert
        assertEquals(skuProduct, stock.getSkuProduct());
    }

    @Test
    @DisplayName("Should set and get nameProduct correctly")
    void setAndGetNameProduct() {
        // Arrange
        Stock stock = new Stock();
        String nameProduct = "Smartphone Galaxy X20";

        // Act
        stock.setNameProduct(nameProduct);

        // Assert
        assertEquals(nameProduct, stock.getNameProduct());
    }

    @Test
    @DisplayName("Should set and get quantity correctly")
    void setAndGetQuantity() {
        // Arrange
        Stock stock = new Stock();
        Long quantity = 50L;

        // Act
        stock.setQuantity(quantity);

        // Assert
        assertEquals(quantity, stock.getQuantity());
    }

    @Test
    @DisplayName("Should set and get totalValue correctly")
    void setAndGetTotalValue() {
        // Arrange
        Stock stock = new Stock();
        BigDecimal totalValue = new BigDecimal("49999.50");

        // Act
        stock.setTotalValue(totalValue);

        // Assert
        assertEquals(totalValue, stock.getTotalValue());
    }

    @Test
    @DisplayName("Should set and get valueUnit correctly")
    void setAndGetValueUnit() {
        // Arrange
        Stock stock = new Stock();
        BigDecimal valueUnit = new BigDecimal("999.99");

        // Act
        stock.setValueUnit(valueUnit);

        // Assert
        assertEquals(valueUnit, stock.getValueUnit());
    }

    @Test
    @DisplayName("Should set and get valueSale correctly")
    void setAndGetValueSale() {
        // Arrange
        Stock stock = new Stock();
        BigDecimal valueSale = new BigDecimal("1099.99");

        // Act
        stock.setValueSale(valueSale);

        // Assert
        assertEquals(valueSale, stock.getValueSale());
    }

    @Test
    @DisplayName("Should set and get status correctly")
    void setAndGetStatus() {
        // Arrange
        Stock stock = new Stock();
        StatusEnum status = StatusEnum.AVALIABLE;

        // Act
        stock.setStatus(status);

        // Assert
        assertEquals(status, stock.getStatus());
    }

    @Test
    @DisplayName("Should set and get dtRegister correctly")
    void setAndGetDtRegister() {
        // Arrange
        Stock stock = new Stock();
        LocalDateTime dtRegister = LocalDateTime.now();

        // Act
        stock.setDtRegister(dtRegister);

        // Assert
        assertEquals(dtRegister, stock.getDtRegister());
    }
}