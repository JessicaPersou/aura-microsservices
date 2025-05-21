package com.postech.auramsorder.adapter.controller;

import com.postech.auramsorder.adapter.dto.OrderRequestDTO;
import com.postech.auramsorder.adapter.dto.OrderResponseDTO;
import com.postech.auramsorder.application.ProcessOrderUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    public final ProcessOrderUseCase processOrderUseCase;

    public OrderController(ProcessOrderUseCase processOrderUseCase) {
        this.processOrderUseCase = processOrderUseCase;
    }

    @PostMapping("/new-solicitation")
    public OrderResponseDTO clientExist(@RequestBody OrderRequestDTO orderRequestDTO) {
        return processOrderUseCase.process(orderRequestDTO);
    }
}
