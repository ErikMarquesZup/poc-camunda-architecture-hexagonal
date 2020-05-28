package br.com.application.ports;

import br.com.management.kafka.message.MessageKafka;

import java.io.IOException;

public interface ProducerKafkaPort {

    void sendToKafka(MessageKafka message) throws IOException;
}
