package com.postech.auramsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuraMsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuraMsClientApplication.class, args);
    }

}

/**
 * A estrutura de pacotes do projeto é organizada da seguinte forma:
 *
 * - adapters: Controladores e DTOs
 * - application: Casos de uso
 * - config: Configuração da aplicação
 * - domain: Entidades e objetos de valor
 * - gateway: Repositórios e infraestrutura
 *
 * A estrutura de diretórios do projeto é organizada da seguinte forma:
 *
 * src/
 *  └── main/
 *      ├── java/com/postech/auramsclient
 *      │   ├── adapters       # Controladores e DTOs
 *      │   ├── application    # Casos de uso
 *      │   ├── config         # Configuração da aplicação
 *      │   ├── domain         # Entidades e objetos de valor
 *      │   └── gateway        # Repositórios e infraestrutura
 *      └── resources/
 *          ├── api            # Especificação OpenAPI
 *          └── db/migration   # Migrações Flyway
 *  └── test/
 *      └── java/...           # Casos de teste
 */
