package com.postech.auramspayment.application;

import com.postech.auramspayment.adapter.controller.dto.PaymentRequestDTO;
import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.config.exception.ProcessPaymentException;
import com.postech.auramspayment.domain.Payment;
import com.postech.auramspayment.domain.enums.PaymentStatus;
import com.postech.auramspayment.gateway.PaymentRepository;
import com.postech.auramspayment.gateway.external.PaymentGatewayAdapter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayAdapter paymentGatewayAdapter;

    public ProcessPaymentUseCase(PaymentRepository paymentRepository,
                                 PaymentGatewayAdapter paymentGatewayAdapter) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayAdapter = paymentGatewayAdapter;
    }

    public PaymentResponseDTO execute(PaymentRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new IllegalArgumentException("O request de pagamento não pode ser nulo");
        }

        validateRequest(requestDTO);

        try {
            Payment payment = new Payment(
                    null,
                    requestDTO.getOrderId(),
                    requestDTO.getCardNumber(),
                    requestDTO.getAmount(),
                    PaymentStatus.PENDING,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            PaymentStatus processStatus = paymentGatewayAdapter.processPayment(payment);
            payment.setStatus(processStatus);

            Payment savedPayment = paymentRepository.save(payment);

            String message = (processStatus == PaymentStatus.COMPLETED)
                    ? "Pagamento processado com sucesso"
                    : "Falha no processamento do pagamento";

            return new PaymentResponseDTO(
                    savedPayment.getOrderId(),
                    savedPayment.getStatus(),
                    message,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            throw new ProcessPaymentException("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    private void validateRequest(PaymentRequestDTO requestDTO) {
        if (requestDTO.getOrderId() == null ) {
            throw new IllegalArgumentException("ID do pedido não pode ser vazio");
        }

        if (requestDTO.getCardNumber() == null || requestDTO.getCardNumber().isBlank()) {
            throw new IllegalArgumentException("Número do cartão não pode ser vazio");
        }

        if (requestDTO.getAmount() == null || requestDTO.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("Valor do pagamento deve ser maior que zero");
        }
    }
}