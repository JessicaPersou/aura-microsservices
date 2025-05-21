package com.postech.auramsproduct;

import org.springframework.boot.SpringApplication;

public class TestAuraMsProductApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuraMsProductApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
