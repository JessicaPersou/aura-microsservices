package com.postech.auramsstock.adapters.dto;

public class ReserveStockDTO {
    private String sku;
    private Long quantity;

    public ReserveStockDTO() {
    }

    public ReserveStockDTO(String sku, Long quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
