package com.postech.auramspayment.adapter.controller.dto;

import com.postech.auramspayment.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponseDTO {
    private Long orderId;
    private PaymentStatus status;
    private String message;
    private LocalDateTime processedAt;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(Long orderId, PaymentStatus status, String message, LocalDateTime processedAt) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.processedAt = processedAt != null ? processedAt : LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}