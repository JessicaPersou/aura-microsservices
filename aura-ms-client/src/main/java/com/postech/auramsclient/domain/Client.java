package com.postech.auramsclient.domain;

import com.postech.auramsclient.domain.valueobject.CPF;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

public class Client {
    private Long id;
    private String firstName;
    private String lastName;
    private CPF cpf;
    private LocalDate birthDate;
    private List<Address> addresses;

    public Client() {
    }

    public Client(String firstName, String lastName, CPF cpf, LocalDate birthDate, List<Address> addresses) {
        this.firstName = Objects.requireNonNull(firstName, "Nome não pode ser nulo");
        this.lastName = Objects.requireNonNull(lastName, "Sobrenome não pode ser nulo");
        this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo");
        this.birthDate = Objects.requireNonNull(birthDate, "Data de nascimento não pode ser nulo");
        this.addresses = Objects.requireNonNull(addresses, "Endereço não pode ser nulo");
        validate();
    }

    public boolean isAdult() {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    private void validate() {
        if (!cpf.isValid()) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Sobrenome não pode ser vazio");
        }
        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("Endereço não pode ser vazio");
        }
        if (!isAdult()) {
            throw new IllegalArgumentException("Cliente deve ter81 anos, ou mais.");
        }
    }

    public void addAddress(Address address) {
        Objects.requireNonNull(address, "Endereço não pode ser nulo");
        this.addresses.add(address);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CPF getCpf() {
        return cpf;
    }

    public void setCpf(CPF cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}