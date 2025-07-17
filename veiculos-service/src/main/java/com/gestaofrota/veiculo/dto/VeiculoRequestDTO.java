package com.gestaofrota.veiculo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;


@Value
public class VeiculoRequestDTO {

    @NotBlank(message = "O campo 'placa' é obrigatório.")
    String placa;

    @NotBlank(message = "O campo 'modelo' é obrigatório.")
    String modelo;

    @NotNull(message = "O campo 'anoFabricacao' é obrigatório.")
    Integer anoFabricacao;

    @NotNull(message = "O campo 'hodometro' é obrigatório.")
    @PositiveOrZero(message = "O hodômetro deve ser maior ou igual a zero.")
    Double hodometro;


}