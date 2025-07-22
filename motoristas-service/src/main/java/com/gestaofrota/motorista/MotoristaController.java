package com.gestaofrota.motorista;

import com.gestaofrota.motorista.dto.MotoristaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    private final MotoristaRepository motoristaRepository;

    public MotoristaController(MotoristaRepository motoristaRepository) {
        this.motoristaRepository = motoristaRepository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid MotoristaRequestDTO dto, UriComponentsBuilder uriBuilder) {
        if (motoristaRepository.findByCnh(dto.getCnh()).isPresent()) {
            return ResponseEntity.status(409).body("Conflito: CNH já cadastrada.");
        }

        Motorista motorista = new Motorista(null, dto.getNome(), dto.getCnh());
        Motorista motoristaSalvo = motoristaRepository.save(motorista);

        URI location = uriBuilder.path("/motoristas/{id}").buildAndExpand(motoristaSalvo.getId()).toUri();

        return ResponseEntity.created(location).body(motoristaSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motorista> buscarPorId(@PathVariable Long id) {
        return motoristaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}