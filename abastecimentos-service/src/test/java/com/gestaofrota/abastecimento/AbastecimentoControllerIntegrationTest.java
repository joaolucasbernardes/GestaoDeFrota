package com.gestaofrota.abastecimento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaofrota.abastecimento.dto.AbastecimentoRequestDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AbastecimentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @Test
    void deveRegistrarAbastecimentoComSucesso() throws Exception {
        var abastecimentoDTO = new AbastecimentoRequestDTO(1L, 1L, 15000.0, 40.0, 5.50, TipoCombustivel.GASOLINA);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String veiculoJson = "{\"id\":1, \"placa\":\"ABC-1234\", \"hodometro\":14500.0}";
        mockServer.expect(requestTo("http://localhost:8080/veiculos/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(veiculoJson, MediaType.APPLICATION_JSON));

        String motoristaJson = "{\"id\":1, \"nome\":\"João\"}";
        mockServer.expect(requestTo("http://localhost:8081/motoristas/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(motoristaJson, MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/abastecimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(abastecimentoDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(kafkaProducerService).enviarAbastecimento(Mockito.any(Abastecimento.class));
    }
}