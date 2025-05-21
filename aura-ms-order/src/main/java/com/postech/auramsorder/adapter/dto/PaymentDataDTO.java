package com.postech.auramsorder.adapter.dto;


public class PaymentDataDTO {
    private String creditCardNumber;

    public PaymentDataDTO() {
    }

    public PaymentDataDTO(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

}
