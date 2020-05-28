package br.com.hexagonal.architecture.journey.consumer;

import br.com.management.kafka.message.StartJourney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import javax.inject.Named;

@Named
@KafkaListener(topics = {"command-topics"})
@Slf4j
public class EventConsumerImpl implements EventConsumer {

    @Override
    @KafkaHandler
    public void consume(StartJourney startJourney) {
        log.info("Consuming Message!");
    }
}
