package com.postech.auramsorderreceiver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long clientId;
    private List<OrderItem> items;
    private PaymentData paymentData;
}

