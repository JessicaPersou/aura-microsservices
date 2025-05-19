package com.postech.auramspayment.adapter.controller.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private Long orderId;
    private String cardNumber;
    private BigDecimal amount;
    private Long clientId;

    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(Long orderId, String cardNumber, BigDecimal amount, Long clientId) {
        this.orderId = orderId;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.clientId = clientId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}