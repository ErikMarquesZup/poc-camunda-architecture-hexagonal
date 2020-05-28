package br.com.hexagonal.architecture.journey.service;

import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.management.kafka.constants.TypeComponent;
import br.com.management.kafka.message.MessageKafka;
import br.com.management.kafka.message.StartJourney;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class StartJourneyService {

    @Autowired
    private ProducerKafkaPort kafkaService;
    @Autowired
    private ObjectMapper objectMapper;

    public StartJourneyService(ProducerKafkaPort kafkaService, ObjectMapper objectMapper) {
        this.kafkaService = kafkaService;
        this.objectMapper = objectMapper;
    }

    public String execute(JourneyStartRequest requestStart) throws IOException {
        String uuid = UUID.randomUUID().toString();

        StartJourney startJourney = StartJourney.builder()
                .type(TypeComponent.START_EVENT.getEvent())
                .cpf(requestStart.getCpf())
                .internalUserTask(Boolean.TRUE)
                .bpmnInstance(requestStart.getBpmnInstance()).build();

        startJourney.setUuid(uuid);

        MessageKafka message = MessageKafka.builder()
                                           .payload(startJourney)
                                           .topic("command-topics")
                                           .clazz(StartJourney.class)
                                           .uuid(uuid).build();

        kafkaService.sendToKafka(message);
        return null;
    }
}
