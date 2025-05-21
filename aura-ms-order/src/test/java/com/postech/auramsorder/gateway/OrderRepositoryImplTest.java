package com.postech.auramsorder.gateway;

import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import com.postech.auramsorder.gateway.database.jpa.repository.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    private OrderEntity orderEntity;
    private OrderEntity savedEntity;
    private OrderEntity mappedEntity;

    @BeforeEach
    void setUp() {
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setStatus("PENDING");

        savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        savedEntity.setStatus("SAVED");

        mappedEntity = new OrderEntity();
        mappedEntity.setId(1L);
        mappedEntity.setStatus("MAPPED");
    }

    @Test
    void save_ShouldSaveAndMapEntity() {
        // Arrange
        when(orderJpaRepository.save(orderEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, OrderEntity.class)).thenReturn(mappedEntity);

        // Act
        OrderEntity result = orderRepository.save(orderEntity);

        // Assert
        assertNotNull(result);
        assertEquals(mappedEntity, result);
        verify(orderJpaRepository).save(orderEntity);
        verify(modelMapper).map(savedEntity, OrderEntity.class);
    }

    @Test
    void findById_ShouldReturnMappedEntityWhenExists() {
        // Arrange
        when(orderJpaRepository.findById(1L)).thenReturn(Optional.of(savedEntity));
        when(modelMapper.map(savedEntity, OrderEntity.class)).thenReturn(mappedEntity);

        // Act
        Optional<OrderEntity> result = orderRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mappedEntity, result.get());
        verify(orderJpaRepository).findById(1L);
        verify(modelMapper).map(savedEntity, OrderEntity.class);
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        // Arrange
        when(orderJpaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<OrderEntity> result = orderRepository.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(orderJpaRepository).findById(1L);
        verify(modelMapper, never()).map(any(), eq(OrderEntity.class));
    }

    @Test
    void findAll_ShouldReturnMappedList() {
        // Arrange
        List<OrderEntity> entities = Arrays.asList(savedEntity, savedEntity);
        List<OrderEntity> mappedEntities = Arrays.asList(mappedEntity, mappedEntity);

        when(orderJpaRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(entities, List.class)).thenReturn(mappedEntities);

        // Act
        List<OrderEntity> result = orderRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mappedEntities, result);
        verify(orderJpaRepository).findAll();
        verify(modelMapper).map(entities, List.class);
    }

    @Test
    void findByStatus_ShouldReturnMappedList() {
        // Arrange
        String status = "PENDING";
        List<OrderEntity> entities = Arrays.asList(savedEntity);
        List<OrderEntity> mappedEntities = Arrays.asList(mappedEntity);

        when(orderJpaRepository.findByStatus(status)).thenReturn(entities);
        when(modelMapper.map(entities, List.class)).thenReturn(mappedEntities);

        // Act
        List<OrderEntity> result = orderRepository.findByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mappedEntities, result);
        verify(orderJpaRepository).findByStatus(status);
        verify(modelMapper).map(entities, List.class);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        // Act
        orderRepository.deleteById(1L);

        // Assert
        verify(orderJpaRepository).deleteById(1L);
        verifyNoMoreInteractions(orderJpaRepository, modelMapper);
    }
}