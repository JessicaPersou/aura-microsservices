package com.postech.auramsorder.adapter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderResponseDTO {
    private String fullName;
    private UUID numberOfOrder;
    private String status;
    private List<RequestStockReserveDTO> items;
    private BigDecimal totalAmount;
    private LocalDateTime dtOrder;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(String fullName, UUID numberOfOrder, String status, List<RequestStockReserveDTO> items,
                            BigDecimal totalAmount, LocalDateTime dtOrder) {
        this.fullName = fullName;
        this.numberOfOrder = numberOfOrder;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
        this.dtOrder = dtOrder;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UUID getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(UUID numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RequestStockReserveDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestStockReserveDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getDtOrder() {
        return dtOrder;
    }

    public void setDtOrder(LocalDateTime dtOrder) {
        this.dtOrder = dtOrder;
    }
}
