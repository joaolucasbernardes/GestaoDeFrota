package com.gestaofrota.motorista.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class MotoristaRequestDTO {

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    String nome;

    @NotBlank(message = "O campo 'cnh' é obrigatório.")
    String cnh;

}