package com.postech.auramspayment.gateway.database.jpa.repository;

import com.postech.auramspayment.gateway.database.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT p FROM PaymentEntity p WHERE p.orderId = ?1")
    PaymentEntity findByOrderId(Long orderId);
}
