package com.postech.auramsorder.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private Long id;
    private Long clientId;
    private String items;
    private LocalDateTime dtCreate;
    private String status;
    private BigDecimal totalAmount;
    private Long paymentId;
    private String paymentCardNumber;

    public Order() {
    }

    public Order(Long id, Long clientId, String items, LocalDateTime dtCreate, String status, BigDecimal totalAmount,
                 Long paymentId, String paymentCardNumber) {
        this.id = id;
        this.clientId = clientId;
        this.items = items;
        this.dtCreate = dtCreate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentId = paymentId;
        this.paymentCardNumber = paymentCardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentCardNumber() {
        return paymentCardNumber;
    }

    public void setPaymentCardNumber(String paymentCardNumber) {
        this.paymentCardNumber = paymentCardNumber;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", items='" + items + '\'' +
                ", dtCreate=" + dtCreate +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentId=" + paymentId +
                ", paymentCardNumber='" + paymentCardNumber + '\'' +
                '}';
    }
}