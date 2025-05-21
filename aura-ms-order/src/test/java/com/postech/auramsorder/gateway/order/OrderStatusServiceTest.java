package com.postech.auramsorder.gateway.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.adapter.dto.OrderRequestDTO;
import com.postech.auramsorder.adapter.dto.PaymentDataDTO;
import com.postech.auramsorder.adapter.dto.RequestStockReserveDTO;
import com.postech.auramsorder.config.exception.OrderFailSerealizationItems;
import com.postech.auramsorder.domain.Order;
import com.postech.auramsorder.gateway.OrderRepository;
import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderStatusServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private OrderStatusService orderStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOpenOrder_Success() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));
        orderRequestDTO.setPaymentData(new PaymentDataDTO("1234567890123456"));

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setClientId(1L);
        savedEntity.setStatus("ABERTO");
        savedEntity.setDtCreate(LocalDateTime.now());
        savedEntity.setTotalAmount(new BigDecimal("200.00"));

        Order mappedOrder = new Order();
        mappedOrder.setId(1L);
        mappedOrder.setClientId(1L);
        mappedOrder.setStatus("ABERTO");
        mappedOrder.setDtCreate(LocalDateTime.now());
        mappedOrder.setTotalAmount(new BigDecimal("200.00"));

        when(objectMapper.writeValueAsString(any())).thenReturn("[{\"quantity\":2,\"description\":\"Produto A\"}]");
        when(modelMapper.map(any(Order.class), eq(OrderEntity.class))).thenReturn(savedEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);
        when(modelMapper.map(any(OrderEntity.class), eq(Order.class))).thenReturn(mappedOrder);

        Order result = orderStatusService.createOpenOrder(orderRequestDTO);

        assertNotNull(result);
        assertEquals("ABERTO", result.getStatus());
        assertEquals(1L, result.getClientId());
        assertEquals(new BigDecimal("200.00"), result.getTotalAmount());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testCreateOpenOrder_FailSerialization() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Erro de serialização") {
        });

        OrderFailSerealizationItems exception = assertThrows(OrderFailSerealizationItems.class, () -> {
            orderStatusService.createOpenOrder(orderRequestDTO);
        });

        assertEquals("Falha ao serealizar items do pedido", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }
}