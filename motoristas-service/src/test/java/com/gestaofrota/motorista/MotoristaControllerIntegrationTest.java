package com.gestaofrota.motorista;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaofrota.motorista.dto.MotoristaRequestDTO;
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
public class MotoristaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarMotoristaComSucesso() throws Exception {
        var motoristaDTO = new MotoristaRequestDTO(
                "Motorista de Teste",
                "11223344556"
        );

        mockMvc.perform(post("/motoristas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(motoristaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Motorista de Teste"));
    }

    @Test
    void naoDeveCadastrarMotoristaComCnhDuplicada() throws Exception {
        var motoristaInicial = new MotoristaRequestDTO("Primeiro Motorista", "99887766554");
        mockMvc.perform(post("/motoristas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(motoristaInicial)))
                .andExpect(status().isCreated());

        var motoristaDuplicado = new MotoristaRequestDTO("Segundo Motorista", "99887766554");
        mockMvc.perform(post("/motoristas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(motoristaDuplicado)))
                .andExpect(status().isConflict());
    }
}