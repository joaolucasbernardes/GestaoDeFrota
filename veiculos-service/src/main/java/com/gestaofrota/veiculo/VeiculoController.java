package com.gestaofrota.veiculo;

import com.gestaofrota.veiculo.dto.VeiculoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
            return ResponseEntity.status(409).body("Erro: Placa já cadastrada.");
        }

        Veiculo veiculo = new Veiculo(null, dto.getPlaca(), dto.getModelo(), dto.getAnoFabricacao(), dto.getHodometro());
        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        URI location = uriBuilder.path("/veiculos/{id}").buildAndExpand(veiculoSalvo.getId()).toUri();

        return ResponseEntity.created(location).body(veiculoSalvo);
    }
}