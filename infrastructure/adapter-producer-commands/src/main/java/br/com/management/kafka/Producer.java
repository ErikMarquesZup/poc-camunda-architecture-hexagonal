package br.com.management.kafka;

import message.model.MessageKafka;

import java.io.IOException;

public interface Producer {

    void sendToKafka(MessageKafka message) throws IOException;

}
