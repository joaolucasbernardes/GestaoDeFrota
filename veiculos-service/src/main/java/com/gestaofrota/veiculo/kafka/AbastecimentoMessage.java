package com.gestaofrota.veiculo.kafka;

public record AbastecimentoMessage(
        Long id,
        Long veiculoId,
        Long motoristaId,
        Double hodometro,
        Double litros,
        Double valorPorLitro,
        TipoCombustivel tipoCombustivel
) {}