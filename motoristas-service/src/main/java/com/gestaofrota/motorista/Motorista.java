package com.gestaofrota.motorista;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Motorista {

    private Long id;
    private String nome;
    private String cnh;

}