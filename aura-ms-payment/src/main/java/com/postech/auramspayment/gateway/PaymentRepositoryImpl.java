package com.postech.auramspayment.gateway;

import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.gateway.database.jpa.entity.PaymentEntity;
import com.postech.auramspayment.gateway.database.jpa.repository.PaymentJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final ModelMapper modelMapper;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository, ModelMapper modelMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = modelMapper.map(payment, PaymentEntity.class);
        entity = paymentJpaRepository.save(entity);
        return modelMapper.map(entity, Payment.class);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        PaymentEntity entity = paymentJpaRepository.findByOrderId(orderId);
        if (entity != null) {
            Payment payment = modelMapper.map(entity, Payment.class);
            return Optional.of(payment);
        }
        return Optional.empty();
    }
}