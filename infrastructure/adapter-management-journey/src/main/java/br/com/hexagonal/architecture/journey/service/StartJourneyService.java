package br.com.hexagonal.architecture.journey.service;

import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.constants.TypeComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.model.DomainEvent;
import message.model.MessageKafka;
import message.model.StartJourney;
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

        MessageKafka message = MessageKafka.builder()
                                           .payload(StartJourney.builder()
                                                   .type(TypeComponent.START_EVENT.getEvent())
                                                   .cpf(requestStart.getCpf())
                                                   .uuid(uuid)
                                                   .bpmnInstance(requestStart.getBpmnInstance()).build())
                                           .topic("command-topics")
                                           .uuid(uuid).build();

        kafkaService.sendToKafka(message);
        return null;
    }
}
