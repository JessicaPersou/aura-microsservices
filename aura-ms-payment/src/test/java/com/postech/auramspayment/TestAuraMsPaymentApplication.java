package com.postech.auramspayment;

import org.springframework.boot.SpringApplication;

public class TestAuraMsPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuraMsPaymentApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
