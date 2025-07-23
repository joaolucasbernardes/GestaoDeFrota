package com.gestaofrota;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API de Gestão de Frotas",
        version = "1.0",
        description = "Microserviço responsável por realizar abastecimentos."
))
public class AbastecimentosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbastecimentosServiceApplication.class, args);
    }
}