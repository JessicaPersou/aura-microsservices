package com.postech.auramspayment.gateway.external;

import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MockPaymentGatewayAdapter implements PaymentGatewayAdapter {

    private final Map<String, PaymentStatus> paymentStatusCache = new HashMap<>();

    @Override
    public PaymentStatus processPayment(Payment payment) {
        if (payment == null || payment.getCardNumber() == null) {
            return PaymentStatus.FAILED;
        }

        PaymentStatus status;

        if (payment.getCardNumber().startsWith("4")) {
            status = PaymentStatus.COMPLETED;
        }

        else if (payment.getCardNumber().startsWith("5")) {
            status = PaymentStatus.FAILED;
        }

        else {
            status = Math.random() > 0.3 ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;
        }


        paymentStatusCache.put(payment.getOrderId().toString(), status);

        return status;
    }

    @Override
    public PaymentStatus checkPaymentStatus(String orderId) {
        return paymentStatusCache.getOrDefault(orderId, PaymentStatus.PENDING);
    }
}
