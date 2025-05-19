package com.postech.auramsstock.database.jpa.entity;

import com.postech.auramsstock.domain.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "STOCK")
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SKU_PRODUCT", unique = true, nullable = false, length = 20)
    private String skuProduct;

    @Column(name = "NAME_PRODUCT", nullable = false, length = 100)
    private String nameProduct;

    @Column(nullable = false, precision = 10, scale = 2)
    private Long quantity;

    @Column(name = "TOTAL_VALUE", precision = 10, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "VALUE_UNIT", precision = 10, scale = 2)
    private BigDecimal valueUnit;

    @Column(name = "VALUE_SALE", precision = 10, scale = 2)
    private BigDecimal valueSale;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusEnum status;

    @Column(name = "DT_REGISTER", nullable = false, updatable = false)
    private LocalDateTime dtRegister;

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

    public void setQuantity(Long quantityActual) {
        this.quantity = quantityActual;
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