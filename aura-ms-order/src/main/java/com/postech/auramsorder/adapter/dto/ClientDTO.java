package com.postech.auramsorder.adapter.dto;

public class ClientDTO {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String cpf;

    public ClientDTO(){}

    public ClientDTO(Long clientId, String firstName, String lastName, String cpf) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
