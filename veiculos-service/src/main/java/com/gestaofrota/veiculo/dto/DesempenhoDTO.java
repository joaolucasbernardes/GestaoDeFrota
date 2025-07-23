package com.gestaofrota.veiculo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DesempenhoDTO {
    private Long veiculoId;
    private String placa;
    private Double consumoMedioKmLitro;
}