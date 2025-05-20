package com.postech.auramsorder;

import org.springframework.boot.SpringApplication;

public class TestAuraMSOrderApplication {

    public static void main(String[] args) {
        SpringApplication.from(TestAuraMSOrderApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}