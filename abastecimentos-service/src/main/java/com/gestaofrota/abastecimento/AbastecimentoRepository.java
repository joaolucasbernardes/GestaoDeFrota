package com.gestaofrota.abastecimento;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AbastecimentoRepository {
    private final Map<Long, Abastecimento> abastecimentos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Abastecimento save(Abastecimento abastecimento) {
        long novoId = idGenerator.incrementAndGet();
        abastecimento.setId(novoId);
        abastecimentos.put(novoId, abastecimento);
        return abastecimento;
    }
}