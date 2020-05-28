package br.com.hexagonal.architecture.journey.consumer;

import br.com.management.kafka.message.StartJourney;

import java.io.IOException;

public interface EventConsumer {

    void consume(StartJourney startJourney) throws IOException;

}
