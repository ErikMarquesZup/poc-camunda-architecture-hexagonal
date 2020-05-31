package br.com.application.ports;

import message.model.MessageKafka;

import java.io.IOException;

public interface ProducerKafkaPort {

    void sendToKafka(MessageKafka message) throws IOException;
}
