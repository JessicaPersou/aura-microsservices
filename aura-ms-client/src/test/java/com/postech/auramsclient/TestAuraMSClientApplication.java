package com.postech.auramsclient;

import org.springframework.boot.SpringApplication;

public class TestAuraMSClientApplication {

    public static void main(String[] args) {
        SpringApplication.from(TestAuraMSClientApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
