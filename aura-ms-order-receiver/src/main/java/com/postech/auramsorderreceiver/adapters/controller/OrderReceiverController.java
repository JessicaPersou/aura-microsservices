package com.postech.auramsorderreceiver.adapters.controller;


import com.postech.auramsorderreceiver.adapters.dto.OrderRequestDTO;
import com.postech.auramsorderreceiver.adapters.dto.OrderResponseDTO;
import com.postech.auramsorderreceiver.config.modelmapper.OrderModelMapper;
import com.postech.auramsorderreceiver.application.ReceiveOrderUseCase;
import com.postech.auramsorderreceiver.domain.OrderRequest;
import com.postech.auramsorderreceiver.domain.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderReceiverController {

    private final ReceiveOrderUseCase receiveOrderUseCase;
    private final OrderModelMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> receiveOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            OrderRequest orderRequest = orderMapper.toOrderRequest(orderRequestDTO);
            String messageId = receiveOrderUseCase.receiveOrder(orderRequest);

            OrderResponseDTO response = OrderResponseDTO.builder()
                    .messageId(messageId)
                    .status(OrderStatus.ABERTO.name())
                    .build();

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid order request", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error processing order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}