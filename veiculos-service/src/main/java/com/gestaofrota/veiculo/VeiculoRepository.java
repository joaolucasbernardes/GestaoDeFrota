package com.gestaofrota.veiculo;

import com.gestaofrota.veiculo.kafka.AbastecimentoMessage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class VeiculoRepository {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VeiculoRepository.class);

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

    public Optional<Veiculo> findById(Long id) {
        return Optional.ofNullable(veiculos.get(id));
    }

    public void atualizaHodometro(Long id, Double novoHodometro) {
        log.info("Tentando atualizar hodômetro para o veículo de ID: {}", id);
        Optional<Veiculo> veiculoOpt = findById(id);

        if (veiculoOpt.isPresent()) {
            Veiculo veiculo = veiculoOpt.get();
            veiculo.setHodometro(novoHodometro);
            log.info("Hodômetro do veículo {} atualizado com sucesso para {}.", id, novoHodometro);
        } else {
            log.warn("Veículo com ID {} não encontrado para atualização de hodômetro.", id);
        }
    }

    public void adicionarAbastecimento(Long veiculoId, AbastecimentoMessage abastecimento) {
        log.info("Tentando adicionar abastecimento ao histórico do veículo de ID: {}", veiculoId);
        findById(veiculoId).ifPresent(veiculo -> {
            veiculo.setHodometro(abastecimento.hodometro());
            veiculo.getHistoricoAbastecimentos().add(abastecimento);

            log.info("Abastecimento adicionado e hodômetro do veículo {} atualizado.", veiculoId);
        });
    }
}