package com.postech.auramsorderreceiver.config.modelmapper;


import com.postech.auramsorderreceiver.adapters.dto.OrderItemDTO;
import com.postech.auramsorderreceiver.adapters.dto.OrderRequestDTO;
import com.postech.auramsorderreceiver.adapters.dto.PaymentDataDTO;
import com.postech.auramsorderreceiver.domain.OrderItem;
import com.postech.auramsorderreceiver.domain.OrderRequest;
import com.postech.auramsorderreceiver.domain.PaymentData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderModelMapper {

    public OrderRequest toOrderRequest(OrderRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return OrderRequest.builder()
                .clientId(dto.getClientId())
                .items(mapItems(dto.getItems()))
                .paymentData(mapPaymentData(dto.getPaymentData()))
                .build();
    }

    private List<OrderItem> mapItems(List<OrderItemDTO> dtoItems) {
        if (dtoItems == null) {
            return null;
        }

        return dtoItems.stream()
                .map(item -> OrderItem.builder()
                        .sku(item.getSku())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private PaymentData mapPaymentData(PaymentDataDTO dto) {
        if (dto == null) {
            return null;
        }

        return PaymentData.builder()
                .creditCardNumber(dto.getCreditCardNumber())
                .build();
    }
}
