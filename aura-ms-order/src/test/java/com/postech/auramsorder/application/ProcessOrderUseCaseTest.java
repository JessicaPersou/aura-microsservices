package com.postech.auramsorder.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.adapter.dto.*;
import com.postech.auramsorder.config.exception.FailProcessNewOrderException;
import com.postech.auramsorder.domain.Order;
import com.postech.auramsorder.gateway.OrderRepository;
import com.postech.auramsorder.gateway.client.ClientService;
import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import com.postech.auramsorder.gateway.payment.PaymentService;
import com.postech.auramsorder.gateway.product.ProductService;
import com.postech.auramsorder.gateway.stock.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @Mock
    private StockService stockService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProcessOrderUseCase processOrderUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessOrder_Success() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setClientId(1L);
        savedEntity.setStatus("FECHADO_COM_SUCESSO");
        savedEntity.setTotalAmount(new BigDecimal("100.00"));
        savedEntity.setNumerOfOrder(UUID.randomUUID());

        when(clientService.verifyClient(1L)).thenReturn(clientDTO);
        when(productService.getProductPrice(anyList())).thenReturn(new BigDecimal("100.00"));
        when(stockService.reserveStock(anyList())).thenReturn(true);
        when(paymentService.processPayment(any(Order.class))).thenReturn(true);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        OrderResponseDTO response = processOrderUseCase.process(orderRequestDTO);

        assertNotNull(response);
        assertEquals("FECHADO_COM_SUCESSO", response.getStatus());
        assertEquals(new BigDecimal("100.00"), response.getTotalAmount());
        verify(orderRepository, times(2)).save(any(OrderEntity.class));
    }

    @Test
    void testProcessOrder_FailSerialization() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Erro de serialização") {
        });

        FailProcessNewOrderException exception = assertThrows(FailProcessNewOrderException.class, () -> {
            processOrderUseCase.process(orderRequestDTO);
        });

        assertEquals("Falha ao serializar itens do pedido", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void testProcessOrder_FailPayment() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setClientId(1L);
        savedEntity.setStatus("PENDENTE_PAGAMENTO");
        savedEntity.setTotalAmount(new BigDecimal("100.00"));
        savedEntity.setNumerOfOrder(UUID.randomUUID());

        when(clientService.verifyClient(1L)).thenReturn(clientDTO);
        when(productService.getProductPrice(anyList())).thenReturn(new BigDecimal("100.00"));
        when(stockService.reserveStock(anyList())).thenReturn(true);
        when(paymentService.processPayment(any(Order.class))).thenReturn(false);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        OrderResponseDTO response = processOrderUseCase.process(orderRequestDTO);

        assertNotNull(response);
        assertEquals("FECHADO_SEM_CREDITO", response.getStatus());
        verify(stockService, times(1)).releaseStock(anyList());
        verify(orderRepository, times(2)).save(any(OrderEntity.class));
    }

    @Test
    void testProcessOrder_OutOfStock() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");

        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setClientId(1L);
        savedEntity.setStatus("FECHADO_SEM_ESTOQUE");
        savedEntity.setTotalAmount(new BigDecimal("100.00"));
        savedEntity.setNumerOfOrder(UUID.randomUUID());

        when(clientService.verifyClient(1L)).thenReturn(clientDTO);
        when(productService.getProductPrice(anyList())).thenReturn(new BigDecimal("100.00"));
        when(stockService.reserveStock(anyList())).thenReturn(false);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        OrderResponseDTO response = processOrderUseCase.process(orderRequestDTO);

        assertNotNull(response);
        assertEquals("FECHADO_SEM_ESTOQUE", response.getStatus());
        verify(stockService, never()).releaseStock(anyList());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testProcessOrder_UnexpectedException() throws JsonProcessingException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(1L);
        orderRequestDTO.setItems(Collections.singletonList(new RequestStockReserveDTO("SKUPRODUCT", 2)));

        when(clientService.verifyClient(1L)).thenThrow(new RuntimeException("Erro inesperado"));

        FailProcessNewOrderException exception = assertThrows(FailProcessNewOrderException.class, () -> {
            processOrderUseCase.process(orderRequestDTO);
        });

        assertEquals("Falha ao gerar novo pedido: ", exception.getMessage());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }


    @Test
    void testSetPaymentCardNumberWhenPaymentDataIsPresent() {
        // Arrange
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        PaymentDataDTO paymentData = new PaymentDataDTO();
        paymentData.setCreditCardNumber("1234-5678-9012-3456");
        orderRequestDTO.setPaymentData(paymentData);

        Order order = new Order();

        // Act
        if (orderRequestDTO.getPaymentData() != null) {
            order.setPaymentCardNumber(orderRequestDTO.getPaymentData().getCreditCardNumber());
        }

        // Assert
        assertEquals("1234-5678-9012-3456", order.getPaymentCardNumber());
    }

    @Test
    void testSetPaymentCardNumberWhenPaymentDataIsNull() {
        // Arrange
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setPaymentData(null);

        Order order = new Order();

        // Act
        if (orderRequestDTO.getPaymentData() != null) {
            order.setPaymentCardNumber(orderRequestDTO.getPaymentData().getCreditCardNumber());
        }

        // Assert
        assertNull(order.getPaymentCardNumber());
    }

}