package com.postech.auramsorderreceiver.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long clientId;
    private List<OrderItemDTO> items;
    private PaymentDataDTO paymentData;
}