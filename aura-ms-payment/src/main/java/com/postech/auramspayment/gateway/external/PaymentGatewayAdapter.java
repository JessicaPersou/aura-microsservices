package com.postech.auramspayment.gateway.external;

import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;

public interface PaymentGatewayAdapter {
    PaymentStatus processPayment(Payment payment);
    PaymentStatus checkPaymentStatus(String orderId);
}
