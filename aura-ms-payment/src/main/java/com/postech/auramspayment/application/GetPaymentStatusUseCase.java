package com.postech.auramspayment.application;

import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import com.postech.auramspayment.gateway.PaymentRepository;
import com.postech.auramspayment.gateway.external.PaymentGatewayAdapter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetPaymentStatusUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayAdapter paymentGatewayAdapter;
    private final ModelMapper modelMapper;

    public GetPaymentStatusUseCase(PaymentRepository paymentRepository,
                                   PaymentGatewayAdapter paymentGatewayAdapter,
                                   ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayAdapter = paymentGatewayAdapter;
        this.modelMapper = modelMapper;
    }

    public PaymentResponseDTO execute(Long orderId) {
        validateOrderId(orderId);

        return paymentRepository.findByOrderId(orderId)
                .map(this::processPaymentStatus)
                .orElseGet(() -> createNotFoundResponse(orderId));
    }

    private void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("ID do pedido não pode ser vazio");
        }
    }

    private PaymentResponseDTO processPaymentStatus(Payment payment) {
        if (payment.getStatus() == PaymentStatus.PENDING) {
            updatePaymentStatusIfNeeded(payment);
        }

        String message = formatMessageByStatus(payment.getStatus());
        return new PaymentResponseDTO(
                payment.getOrderId(),
                payment.getStatus(),
                message,
                payment.getUpdatedAt()
        );
    }

    private void updatePaymentStatusIfNeeded(Payment payment) {
        PaymentStatus updatedStatus = paymentGatewayAdapter.checkPaymentStatus(payment.getOrderId().toString());
        if (updatedStatus != PaymentStatus.PENDING) {
            payment.setStatus(updatedStatus);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }

    private PaymentResponseDTO createNotFoundResponse(Long orderId) {
        return new PaymentResponseDTO(
                orderId,
                PaymentStatus.PENDING,
                "Pagamento não encontrado",
                LocalDateTime.now()
        );
    }

    private String formatMessageByStatus(PaymentStatus status) {
        return switch (status) {
            case COMPLETED -> "Pagamento concluído com sucesso";
            case FAILED -> "Falha no pagamento";
            case REFUNDED -> "Pagamento estornado";
            case PENDING -> "Pagamento em processamento";
        };
    }
}