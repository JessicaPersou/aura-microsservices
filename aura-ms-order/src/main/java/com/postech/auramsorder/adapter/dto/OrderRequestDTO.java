package com.postech.auramsorder.adapter.dto;


import java.util.List;


public class OrderRequestDTO {
    private Long clientId;
    private List<RequestStockReserveDTO> items;
    private PaymentDataDTO paymentData;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long clientId, List<RequestStockReserveDTO> items, PaymentDataDTO paymentData) {
        this.clientId = clientId;
        this.items = items;
        this.paymentData = paymentData;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<RequestStockReserveDTO> getItems() {
        return items;
    }

    public void setItems(List<RequestStockReserveDTO> items) {
        this.items = items;
    }

    public PaymentDataDTO getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(PaymentDataDTO paymentData) {
        this.paymentData = paymentData;
    }
}