package com.gestaofrota.abastecimento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Abastecimento {
    private Long id;
    private Long veiculoId;
    private Long motoristaId;
    private Double hodometro;
    private Double litros;
    private Double valorPorLitro;
    private TipoCombustivel tipoCombustivel;
}