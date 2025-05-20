package com.postech.auramsorder.gateway.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.adapter.dto.OrderRequestDTO;
import com.postech.auramsorder.config.DataSourceConfig;
import com.postech.auramsorder.config.exception.OrderFailSerealizationItems;
import com.postech.auramsorder.domain.Order;
import com.postech.auramsorder.gateway.OrderRepository;
import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderStatusService {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final DataSource dataSource;

    public OrderStatusService(OrderRepository orderRepository, ObjectMapper objectMapper, ModelMapper modelMapper,
                              @Qualifier("orderDataSource") DataSource dataSource) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.dataSource = dataSource;
    }

    @Transactional
    public Order createOpenOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setClientId(orderRequestDTO.getClientId());
        order.setDtCreate(LocalDateTime.now());
        order.setStatus("ABERTO");

        try {
            String itemsJson = objectMapper.writeValueAsString(orderRequestDTO.getItems());
            order.setItems(itemsJson);
        } catch (JsonProcessingException e) {
            throw new OrderFailSerealizationItems("Falha ao serealizar items do pedido", e.getMessage());
        }

        BigDecimal totalAmount = calculateTotalAmount(orderRequestDTO);
        order.setTotalAmount(totalAmount);

        if (orderRequestDTO.getPaymentData() != null) {
            order.setPaymentCardNumber(orderRequestDTO.getPaymentData().getCreditCardNumber());
        }
        OrderEntity entityToSave = modelMapper.map(order, OrderEntity.class);
        orderRepository.save(entityToSave);
        return modelMapper.map(entityToSave, Order.class);
    }

    private BigDecimal calculateTotalAmount(OrderRequestDTO dto) {
        return dto.getItems().stream()
                .map(item -> new BigDecimal(item.getQuantity() * 100))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}