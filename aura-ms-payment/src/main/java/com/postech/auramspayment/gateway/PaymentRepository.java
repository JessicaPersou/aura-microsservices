package com.postech.auramspayment.gateway;

import com.postech.auramspayment.domain.Payment;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(Long orderId);
}
