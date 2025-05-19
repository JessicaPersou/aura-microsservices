package com.postech.auramsorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class AuraMsOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuraMsOrderApplication.class, args);
    }

}