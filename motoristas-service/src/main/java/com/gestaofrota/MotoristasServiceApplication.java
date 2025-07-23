package com.gestaofrota;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API de Gestão de Frotas",
        version = "1.0",
        description = "Microserviço responsável pelo cadastro e consulta de Motoristas."
))
public class MotoristasServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MotoristasServiceApplication.class, args);
    }
}