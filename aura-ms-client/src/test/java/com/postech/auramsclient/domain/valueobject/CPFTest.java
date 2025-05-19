package com.postech.auramsclient.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @Test
    void shouldCreateValidCPF() {
        CPF cpf = new CPF("12345678909");
        assertEquals("12345678909", cpf.getValue());
    }

    @Test
    void shouldThrowExceptionWhenCPFIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new CPF(null);
        });
        assertEquals("CPF não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCPFIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("");
        });
        assertEquals("CPF não pode ser vazio", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "123456789012", "abcdefghijk"})
    void shouldThrowExceptionWhenCPFHasInvalidLength(String invalidCpf) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF(invalidCpf);
        });
        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCPFHasAllDigitsEqual() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("11111111111");
        });
        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    void shouldReturnTrueForValidCPF() {
        CPF cpf = new CPF("80080933076");
        assertTrue(cpf.isValid());
    }

    @Test
    void shouldEqualsSameValueCPF() {
        CPF cpf1 = new CPF("80080933076");
        CPF cpf2 = new CPF("80080933076");

        assertEquals(cpf1, cpf2);
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
    }

    @Test
    void shouldNotEqualsDifferentValueCPF() {
        CPF cpf1 = new CPF("80080933076");
        CPF cpf2 = new CPF("12087670030");

        assertNotEquals(cpf1, cpf2);
        assertNotEquals(cpf1.hashCode(), cpf2.hashCode());
    }

    @Test
    void shouldNotEqualsOtherObject() {
        CPF cpf = new CPF("80080933076");
        Object other = new Object();

        assertNotEquals(cpf, other);
    }
}