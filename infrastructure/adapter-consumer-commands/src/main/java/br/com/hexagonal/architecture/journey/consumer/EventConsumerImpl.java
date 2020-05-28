package br.com.hexagonal.architecture.journey.consumer;

import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.service.EngineClient;
import br.com.management.kafka.message.StartJourney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import javax.inject.Named;
import java.io.IOException;

@Named
@KafkaListener(topics = {"command-topics"})
@Slf4j
public class EventConsumerImpl implements EventConsumer {

    @Autowired
    private EngineClient engineClient;

    @Override
    @KafkaHandler
    public void consume(StartJourney startJourney) throws IOException {
        log.info(":: Consuming Start Event :: UUID {}, CPF {}, BPMN {}", startJourney.getUuid(), startJourney.getCpf(), startJourney.getBpmnInstance());
        engineClient.start(JourneyStartRequest.builder().bpmnInstance(startJourney.getBpmnInstance()).build());
    }
}
