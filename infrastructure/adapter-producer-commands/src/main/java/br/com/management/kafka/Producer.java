package br.com.management.kafka;

import br.com.management.kafka.message.MessageKafka;

import java.io.IOException;

public interface Producer {
    void sendToKafka(MessageKafka message) throws IOException;
}
