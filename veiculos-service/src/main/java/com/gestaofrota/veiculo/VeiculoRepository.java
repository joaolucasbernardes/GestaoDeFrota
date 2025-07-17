package com.gestaofrota.veiculo;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VeiculoRepository {

    private final Map<Long, Veiculo> veiculos = new ConcurrentHashMap<>();

    private final Map<String, Veiculo> indicePorPlaca = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    public Collection<Veiculo> findAll() {
        return veiculos.values();
    }

    public Optional<Veiculo> findByPlaca(String placa) {
        return Optional.ofNullable(indicePorPlaca.get(placa.toUpperCase()));
    }

    public Veiculo save(Veiculo veiculo) {
        long novoId = idGenerator.incrementAndGet();
        veiculo.setId(novoId);

        veiculos.put(novoId, veiculo);
        indicePorPlaca.put(veiculo.getPlaca().toUpperCase(), veiculo);

        return veiculo;
    }
}