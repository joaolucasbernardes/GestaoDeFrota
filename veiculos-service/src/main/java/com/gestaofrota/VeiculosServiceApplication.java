package com.gestaofrota;

import io.swagger.v3.oas.annotations.OpenAPIDefinition; // Importe
import io.swagger.v3.oas.annotations.info.Info;         // Importe
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API de Gestão de Frotas",
        version = "1.0",
        description = "Microserviço responsável pelo cadastro e consulta de veículos da frota."
))
public class VeiculosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VeiculosServiceApplication.class, args);
    }
}