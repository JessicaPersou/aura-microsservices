package com.postech.auramsclient.domain;

import java.util.Objects;

public class Address {
    private Long id;
    private String street;
    private String number;
    private String zipcode;
    private String neighborhood;
    private String city;
    private String state;

    public Address() {
    }

    public Address(String street, String number, String zipcode, String neighborhood, String city, String state) {
        this.street = Objects.requireNonNull(street, "Rua não pode ser nula");
        this.number = Objects.requireNonNull(number, "Número não pode ser nulo");
        this.zipcode = Objects.requireNonNull(zipcode, "CEP não pode ser nulo");
        this.neighborhood = Objects.requireNonNull(neighborhood, "Bairro não pode ser nulo");
        this.city = Objects.requireNonNull(city, "Cidade não pode ser nula");
        this.state = Objects.requireNonNull(state, "Estado não pode ser nulo");

        validate();
    }

    private void validate() {
        if (street.isEmpty()) {
            throw new IllegalArgumentException("Rua não pode ser vazia");
        }
        if (number.isEmpty()) {
            throw new IllegalArgumentException("Número não pode ser vazio");
        }
        if (zipcode.isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        if (neighborhood.isEmpty()) {
            throw new IllegalArgumentException("Bairro não pode ser vazio");
        }
        if (city.isEmpty()) {
            throw new IllegalArgumentException("Cidade não pode ser vazia");
        }
        if (state.isEmpty()) {
            throw new IllegalArgumentException("Estado não pode ser vazio");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
}