package com.gestaofrota.veiculo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaofrota.veiculo.VeiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerListener.class);
    private final VeiculoRepository veiculoRepository;
    private final ObjectMapper objectMapper; // Ferramenta para converter JSON

    public KafkaConsumerListener(VeiculoRepository veiculoRepository, ObjectMapper objectMapper) {
        this.veiculoRepository = veiculoRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "abastecimentos-registrados", groupId = "veiculos-group")
    public void ouvir(String messageJson) {
        try {
            log.info("Mensagem JSON recebida: {}", messageJson);

            AbastecimentoMessage message = objectMapper.readValue(messageJson, AbastecimentoMessage.class);

            log.info("Mensagem deserializada com sucesso: {}", message);

            veiculoRepository.atualizaHodometro(message.veiculoId(), message.hodometro());

        } catch (Exception e) {
            log.error("ERRO CRÍTICO ao deserializar ou processar mensagem do Kafka: {}", messageJson, e);
        }
    }
}