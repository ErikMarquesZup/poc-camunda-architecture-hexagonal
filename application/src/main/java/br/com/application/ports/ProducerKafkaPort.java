package br.com.application.ports;

import br.com.management.kafka.message.MessageKafka;

public interface ProducerKafkaPort {

    void sendToKafka(MessageKafka messageKafka);
}
