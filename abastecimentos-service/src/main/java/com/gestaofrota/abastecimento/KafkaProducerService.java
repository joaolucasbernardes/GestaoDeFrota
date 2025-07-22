package com.gestaofrota.abastecimento;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Abastecimento> kafkaTemplate;
    private final String TOPICO = "abastecimentos-registrados";

    public KafkaProducerService(KafkaTemplate<String, Abastecimento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarAbastecimento(Abastecimento abastecimento) {
        kafkaTemplate.send(TOPICO, abastecimento);
    }
}