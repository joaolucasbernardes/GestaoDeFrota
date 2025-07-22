package com.gestaofrota.abastecimento.dto;

import com.gestaofrota.abastecimento.TipoCombustivel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AbastecimentoRequestDTO(
        @NotNull Long veiculoId,
        @NotNull Long motoristaId,
        @NotNull @Positive Double hodometro,
        @NotNull @Positive Double litros,
        @NotNull @Positive Double valorPorLitro,
        @NotNull TipoCombustivel tipoCombustivel
) {}