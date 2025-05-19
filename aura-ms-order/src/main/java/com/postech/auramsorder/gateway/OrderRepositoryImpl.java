package com.postech.auramsorder.gateway;

import com.postech.auramsorder.gateway.database.jpa.entity.OrderEntity;
import com.postech.auramsorder.gateway.database.jpa.repository.OrderJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final ModelMapper modelMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, ModelMapper modelMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
        return modelMapper.map(savedEntity, OrderEntity.class);
    }

    @Override
    public Optional<OrderEntity> findById(Long id) {
        return orderJpaRepository.findById(id)
                .map(orderEntity -> modelMapper.map(orderEntity, OrderEntity.class));
    }

    @Override
    public List<OrderEntity> findAll() {
        List<OrderEntity> list = orderJpaRepository.findAll();
        return modelMapper.map(list, List.class);
    }

    @Override
    public List<OrderEntity> findByStatus(String status) {
        List<OrderEntity> statusOrderList = orderJpaRepository.findByStatus(status);
        return modelMapper.map(statusOrderList, List.class);
    }

    @Override
    public void deleteById(Long id) {
        orderJpaRepository.deleteById(id);
    }

}