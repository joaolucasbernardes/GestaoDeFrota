package com.gestaofrota.motorista;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MotoristaRepository {

    private final Map<Long, Motorista> motoristas = new ConcurrentHashMap<>();
    private final Map<String, Motorista> indicePorCnh = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Optional<Motorista> findByCnh(String cnh) {
        return Optional.ofNullable(indicePorCnh.get(cnh));
    }

    public Motorista save(Motorista motorista) {
        long novoId = idGenerator.incrementAndGet();
        motorista.setId(novoId);

        motoristas.put(novoId, motorista);
        indicePorCnh.put(motorista.getCnh(), motorista);

        return motorista;
    }
}