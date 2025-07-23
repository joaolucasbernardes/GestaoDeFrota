package com.gestaofrota.veiculo;

import com.gestaofrota.veiculo.dto.VeiculoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.gestaofrota.veiculo.dto.DesempenhoDTO;
import com.gestaofrota.veiculo.kafka.AbastecimentoMessage;
import java.util.Comparator;
import java.util.List;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping
    public ResponseEntity<Collection<Veiculo>> listarTodos() {
        Collection<Veiculo> veiculos = veiculoRepository.findAll();
        return ResponseEntity.ok(veiculos);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid VeiculoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        if (veiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            return ResponseEntity.status(409).body("Conflito: Placa já cadastrada.");
        }

        Veiculo veiculo = new Veiculo(null, dto.getPlaca(), dto.getModelo(), dto.getAnoFabricacao(), dto.getHodometro(), new java.util.ArrayList<>());
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        URI location = uriBuilder.path("/veiculos/{id}").buildAndExpand(veiculoSalvo.getId()).toUri();

        return ResponseEntity.created(location).body(veiculoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/desempenho")
    public ResponseEntity<DesempenhoDTO> calcularDesempenho(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(this::calcularEGerarDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private DesempenhoDTO calcularEGerarDto(Veiculo veiculo) {
        List<AbastecimentoMessage> historico = veiculo.getHistoricoAbastecimentos();
        historico.sort(Comparator.comparing(AbastecimentoMessage::hodometro));

        if (historico.size() < 2) {
            return new DesempenhoDTO(veiculo.getId(), veiculo.getPlaca(), 0.0, 0.0);
        }

        AbastecimentoMessage primeiro = historico.get(0);
        AbastecimentoMessage ultimo = historico.get(historico.size() - 1);

        double distanciaTotal = ultimo.hodometro() - primeiro.hodometro();

        double totalLitros = historico.stream()
                .skip(1)
                .mapToDouble(AbastecimentoMessage::litros)
                .sum();

        double custoTotal = historico.stream()
                .skip(1)
                .mapToDouble(abastecimento -> abastecimento.litros() * abastecimento.valorPorLitro())
                .sum();


        double consumoMedio = (totalLitros > 0) ? distanciaTotal / totalLitros : 0.0;
        double custoMedio = (distanciaTotal > 0) ? custoTotal / distanciaTotal : 0.0;

        consumoMedio = Math.round(consumoMedio * 100.0) / 100.0;
        custoMedio = Math.round(custoMedio * 100.0) / 100.0;

        return new DesempenhoDTO(veiculo.getId(), veiculo.getPlaca(), consumoMedio, custoMedio);
    }

    @GetMapping("/{id}/abastecimentos")
    public ResponseEntity<List<AbastecimentoMessage>> listarAbastecimentos(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(veiculo -> ResponseEntity.ok(veiculo.getHistoricoAbastecimentos()))
                .orElse(ResponseEntity.notFound().build());
    }
}