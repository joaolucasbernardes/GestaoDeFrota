package com.gestaofrota.veiculo;

import com.gestaofrota.veiculo.kafka.AbastecimentoMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private Integer anoFabricacao;
    private Double hodometro;

    private List<AbastecimentoMessage> historicoAbastecimentos = new ArrayList<>();


}