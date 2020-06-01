package br.com.hexagonal.architecture.adapter.out.journey;

import br.com.hexagonal.architecture.domain.model.MessageKafka;

import java.io.IOException;

public interface EventManager {

    void sendToKafka(MessageKafka message) throws IOException;
}
