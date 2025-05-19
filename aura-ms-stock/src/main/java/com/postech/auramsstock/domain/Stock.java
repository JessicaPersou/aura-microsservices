package com.postech.auramsstock.domain;

import com.postech.auramsstock.domain.enums.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Stock {

    private Integer id;
    private String skuProduct;
    private String nameProduct;
    private Long quantity;
    private BigDecimal totalValue;
    private BigDecimal valueUnit;
    private BigDecimal valueSale;
    private StatusEnum status;
    private LocalDateTime dtRegister;

    public Stock() {
    }

    public Stock(Integer id, String skuProduct, String nameProduct, Long quantity, BigDecimal totalValue,
                 BigDecimal valueUnit, BigDecimal valueSale, StatusEnum status, LocalDateTime dtRegister) {
        this.id = id;
        this.skuProduct = skuProduct;
        this.nameProduct = nameProduct;
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.valueUnit = valueUnit;
        this.valueSale = valueSale;
        this.status = status;
        this.dtRegister = dtRegister;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuProduct() {
        return skuProduct;
    }

    public void setSkuProduct(String skuProduct) {
        this.skuProduct = skuProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(BigDecimal valueUnit) {
        this.valueUnit = valueUnit;
    }

    public BigDecimal getValueSale() {
        return valueSale;
    }

    public void setValueSale(BigDecimal valueSale) {
        this.valueSale = valueSale;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getDtRegister() {
        return dtRegister;
    }

    public void setDtRegister(LocalDateTime dtRegister) {
        this.dtRegister = dtRegister;
    }
}
