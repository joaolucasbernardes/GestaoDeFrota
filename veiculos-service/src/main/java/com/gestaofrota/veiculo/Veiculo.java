package com.gestaofrota.veiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private Integer anoFabricacao;
    private Double hodometro;

}