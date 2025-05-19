package com.postech.auramsorder.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsorder.adapter.dto.ClientDTO;
import com.postech.auramsorder.adapter.dto.OrderRequestDTO;
import com.postech.auramsorder.adapter.dto.OrderResponseDTO;
import com.postech.auramsorder.adapter.dto.RequestStockReserveDTO;
import com.postech.auramsorder.config.exception.FailProcessNewOrderException;
import com.postech.auramsorder.domain.Order;
import com.postech.auramsorder.gateway.OrderRepository;
import com.postech.auramsorder.gateway.client.ClientService;
import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import com.postech.auramsorder.gateway.payment.PaymentService;
import com.postech.auramsorder.gateway.product.ProductService;
import com.postech.auramsorder.gateway.stock.StockService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProcessOrderUseCase {


    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final StockService stockService;
    private final PaymentService paymentService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public ProcessOrderUseCase(OrderRepository orderRepository, ClientService clientService,
                               ProductService productService, StockService stockService,
                               PaymentService paymentService, ModelMapper modelMapper,
                               ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.productService = productService;
        this.stockService = stockService;
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderResponseDTO process(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setClientId(orderRequestDTO.getClientId());
        order.setDtCreate(LocalDateTime.now());
        order.setStatus("ABERTO");

        try {
            JsonNode jsonNode = objectMapper.valueToTree(orderRequestDTO.getItems());
            String jsonString = objectMapper.writeValueAsString(jsonNode);
            order.setItems(jsonString);
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar itens do pedido", e);
            throw new FailProcessNewOrderException("Falha ao serializar itens do pedido", e.getMessage());
        }

        if (orderRequestDTO.getPaymentData() != null) {
            order.setPaymentCardNumber(orderRequestDTO.getPaymentData().getCreditCardNumber());
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setClientId(order.getClientId());
        orderEntity.setDtCreate(order.getDtCreate());
        orderEntity.setStatus(order.getStatus());
        orderEntity.setItems(order.getItems());
        orderEntity.setPaymentCardNumber(order.getPaymentCardNumber());
        orderEntity.setNumerOfOrder(UUID.randomUUID());

        try {
            ClientDTO clientDTO = clientService.verifyClient(orderRequestDTO.getClientId());

            List<String> listSku = orderRequestDTO.getItems().stream()
                    .map(RequestStockReserveDTO::getSku)
                    .toList();

            BigDecimal totalOrderValue = productService.getProductPrice(listSku);
            orderEntity.setTotalAmount(totalOrderValue);

            boolean stockAvailable = stockService.reserveStock(orderRequestDTO.getItems());

            if (!stockAvailable) {
                orderEntity.setStatus("FECHADO_SEM_ESTOQUE");
                orderRepository.save(orderEntity);
                return createOrderResponse(orderEntity, clientDTO, orderRequestDTO.getItems());
            }
            orderEntity.setStatus("PENDENTE_PAGAMENTO");
            orderRepository.save(orderEntity);

            Order orderForPayment = new Order();
            orderForPayment.setId(orderEntity.getId());
            orderForPayment.setClientId(orderEntity.getClientId());
            orderForPayment.setItems(orderEntity.getItems());
            orderForPayment.setDtCreate(orderEntity.getDtCreate());
            orderForPayment.setStatus(orderEntity.getStatus());
            orderForPayment.setTotalAmount(orderEntity.getTotalAmount());
            orderForPayment.setPaymentCardNumber(orderEntity.getPaymentCardNumber());

            boolean paymentSuccessful = paymentService.processPayment(orderForPayment);

            if (!paymentSuccessful) {
                stockService.releaseStock(orderRequestDTO.getItems());
                orderEntity.setStatus("FECHADO_SEM_CREDITO");
                orderRepository.save(orderEntity);
                return createOrderResponse(orderEntity, clientDTO, orderRequestDTO.getItems());
            }

            orderEntity.setStatus("FECHADO_COM_SUCESSO");
            orderRepository.save(orderEntity);

            return createOrderResponse(orderEntity, clientDTO, orderRequestDTO.getItems());

        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", e.getMessage(), e);

            Order orderForFailure = new Order();
            orderForFailure.setId(orderEntity.getId());
            orderForFailure.setClientId(orderEntity.getClientId());
            orderForFailure.setItems(orderEntity.getItems());
            orderForFailure.setDtCreate(orderEntity.getDtCreate());
            orderForFailure.setStatus(orderEntity.getStatus());
            orderForFailure.setTotalAmount(orderEntity.getTotalAmount());
            orderForFailure.setPaymentCardNumber(orderEntity.getPaymentCardNumber());

            handleFailure(orderForFailure, orderRequestDTO, e);
            throw new FailProcessNewOrderException("Falha ao gerar novo pedido: ", e.getMessage());
        }
    }

    private OrderResponseDTO createOrderResponse(OrderEntity orderEntity, ClientDTO clientDTO,
                                                 List<RequestStockReserveDTO> items) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setTotalAmount(orderEntity.getTotalAmount());
        response.setStatus(orderEntity.getStatus());
        response.setItems(items);
        response.setFullName(clientDTO.getFirstName() + " " + clientDTO.getLastName());
        response.setNumberOfOrder(orderEntity.getNumerOfOrder());
        response.setDtOrder(orderEntity.getDtCreate() != null ? orderEntity.getDtCreate() : LocalDateTime.now());
        return response;
    }

    private void handleFailure(Order order, OrderRequestDTO orderRequestDTO, Exception e) {
        try {
            stockService.releaseStock(orderRequestDTO.getItems());
            paymentService.refusedIfNecessary(order);

            OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
            orderEntity.setStatus("ERRO");
            orderRepository.save(orderEntity);

            log.error("Pedido com erro: {}", e.getMessage());
        } catch (Exception exceptionInHandling) {
            log.error("Falha ao gerar pedido {}: {}", order.getId(), exceptionInHandling.getMessage());
        }
    }

}

