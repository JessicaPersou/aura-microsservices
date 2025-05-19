package com.postech.auramspayment.adapter.controller;

import com.postech.auramspayment.adapter.controller.dto.PaymentRequestDTO;
import com.postech.auramspayment.adapter.controller.dto.PaymentResponseDTO;
import com.postech.auramspayment.application.GetPaymentStatusUseCase;
import com.postech.auramspayment.application.ProcessPaymentUseCase;
import com.postech.auramspayment.config.exception.ProcessPaymentException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentStatusUseCase getPaymentStatusUseCase;
    private final ModelMapper modelMapper;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase,
                             GetPaymentStatusUseCase getPaymentStatusUseCase,
                             ModelMapper modelMapper) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.getPaymentStatusUseCase = getPaymentStatusUseCase;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = processPaymentUseCase.execute(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new PaymentResponseDTO(
                            request != null ? request.getOrderId() : null,
                            null,
                            e.getMessage(),
                            null
                    )
            );
        } catch (ProcessPaymentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new PaymentResponseDTO(
                            request != null ? request.getOrderId() : null,
                            null,
                            e.getMessage(),
                            null
                    )
            );
        }
    }
}
