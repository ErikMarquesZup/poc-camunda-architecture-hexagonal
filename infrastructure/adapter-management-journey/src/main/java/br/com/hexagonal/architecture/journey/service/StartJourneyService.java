package br.com.hexagonal.architecture.journey.service;

import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.management.kafka.message.MessageKafka;

public class StartJourneyService {

    private ProducerKafkaPort kafkaService;

    public StartJourneyService(ProducerKafkaPort kafkaService) {
        this.kafkaService = kafkaService;
    }

    public String execute(JourneyStartRequest requestStart) {
        MessageKafka messageKafka = MessageKafka.builder()
                                                .bpmnInstance(requestStart.getBpmnInstance())
                                                .cpf(requestStart.getCpf()).build();
        kafkaService.sendToKafka(messageKafka);
        return null;
    }
}
