package com.gestaofrota.abastecimento;

import com.gestaofrota.abastecimento.dto.AbastecimentoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/abastecimentos")
public class AbastecimentoController {

    private final AbastecimentoRepository repository;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducer;

    public AbastecimentoController(AbastecimentoRepository repository, RestTemplate restTemplate, KafkaProducerService kafkaProducer) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody @Valid AbastecimentoRequestDTO dto) {
        Map<String, Object> veiculo;
        try {
            veiculo = restTemplate.getForObject("http://localhost:8080/veiculos/" + dto.veiculoId(), Map.class);
            restTemplate.getForObject("http://localhost:8081/motoristas/" + dto.motoristaId(), Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Veículo ou Motorista não encontrado.");
        }

        if (veiculo == null) return ResponseEntity.status(404).body("Veículo não encontrado.");

        Double hodometroAtual = (Double) veiculo.get("hodometro");
        if (dto.hodometro() <= hodometroAtual) {
            return ResponseEntity.status(400).body("Hodômetro do abastecimento deve ser maior que o hodômetro atual do veículo.");
        }

        Abastecimento abastecimento = new Abastecimento(null, dto.veiculoId(), dto.motoristaId(), dto.hodometro(), dto.litros(), dto.valorPorLitro(), dto.tipoCombustivel());
        repository.save(abastecimento);

        kafkaProducer.enviarAbastecimento(abastecimento);

        return ResponseEntity.status(201).build();
    }
}