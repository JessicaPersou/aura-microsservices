package com.postech.auramsorder.adapter.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        OrderResponseDTO orderResponse = new OrderResponseDTO();
        String fullName = "John Doe";
        UUID numberOfOrder = UUID.randomUUID();
        String status = "COMPLETED";
        List<RequestStockReserveDTO> items = Collections.emptyList();
        BigDecimal totalAmount = BigDecimal.valueOf(150.75);
        LocalDateTime dtOrder = LocalDateTime.now();

        // Act
        orderResponse.setFullName(fullName);
        orderResponse.setNumberOfOrder(numberOfOrder);
        orderResponse.setStatus(status);
        orderResponse.setItems(items);
        orderResponse.setTotalAmount(totalAmount);
        orderResponse.setDtOrder(dtOrder);

        // Assert
        assertEquals(fullName, orderResponse.getFullName());
        assertEquals(numberOfOrder, orderResponse.getNumberOfOrder());
        assertEquals(status, orderResponse.getStatus());
        assertEquals(items, orderResponse.getItems());
        assertEquals(totalAmount, orderResponse.getTotalAmount());
        assertEquals(dtOrder, orderResponse.getDtOrder());
    }

    @Test
    void testConstructorWithParameters() {
        // Arrange
        String fullName = "John Doe";
        UUID numberOfOrder = UUID.randomUUID();
        String status = "COMPLETED";
        List<RequestStockReserveDTO> items = Collections.emptyList();
        BigDecimal totalAmount = BigDecimal.valueOf(150.75);
        LocalDateTime dtOrder = LocalDateTime.now();

        // Act
        OrderResponseDTO orderResponse = new OrderResponseDTO(fullName, numberOfOrder, status, items, totalAmount, dtOrder);

        // Assert
        assertEquals(fullName, orderResponse.getFullName());
        assertEquals(numberOfOrder, orderResponse.getNumberOfOrder());
        assertEquals(status, orderResponse.getStatus());
        assertEquals(items, orderResponse.getItems());
        assertEquals(totalAmount, orderResponse.getTotalAmount());
        assertEquals(dtOrder, orderResponse.getDtOrder());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        OrderResponseDTO orderResponse = new OrderResponseDTO();

        // Assert
        assertNotNull(orderResponse);
    }
}