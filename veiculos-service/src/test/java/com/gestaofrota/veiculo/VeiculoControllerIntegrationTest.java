package com.gestaofrota.veiculo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaofrota.veiculo.dto.VeiculoRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VeiculoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        var veiculoDTO = new VeiculoRequestDTO(
                "TST-9999",
                "Veiculo de Teste",
                2025,
                0.0
        );

        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.placa").value("TST-9999"));
    }

    @Test
    void naoDeveCadastrarVeiculoComPlacaDuplicada() throws Exception {
        var veiculoInicial = new VeiculoRequestDTO("DUP-1234", "Veiculo Original", 2024, 100.0);
        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoInicial)))
                .andExpect(status().isCreated());

        var veiculoDuplicado = new VeiculoRequestDTO("DUP-1234", "Veiculo Duplicado", 2025, 0.0);
        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDuplicado)))
                .andExpect(status().isConflict());
    }
}