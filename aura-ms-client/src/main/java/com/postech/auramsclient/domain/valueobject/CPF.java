package com.postech.auramsclient.domain.valueobject;

import java.util.Objects;

public class CPF {
    private final String document;

    public CPF(String document) {
        this.document = Objects.requireNonNull(document, "CPF não pode ser nulo");
        if (document.isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        if (!isValid()) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }

    public boolean isValid() {
        if (!document.matches("\\d{11}")) {
            return false;
        }
        if (document.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(document.charAt(i)) * (10 - i);
        }

        int remainder = sum % 11;
        int firstCheckDigit = remainder < 2 ? 0 : 11 - remainder;
        if (Character.getNumericValue(document.charAt(9)) != firstCheckDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(document.charAt(i)) * (11 - i);
        }
        remainder = sum % 11;
        int secondCheckDigit = remainder < 2 ? 0 : 11 - remainder;

        return Character.getNumericValue(document.charAt(10)) == secondCheckDigit;
    }

    public String getValue() {
        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CPF cpf = (CPF) o;
        return Objects.equals(document, cpf.document);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(document);
    }
}