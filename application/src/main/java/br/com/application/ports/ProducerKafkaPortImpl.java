package br.com.application.ports;

import br.com.management.kafka.Producer;
import message.model.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProducerKafkaPortImpl implements ProducerKafkaPort {

    @Autowired
    private Producer producerService;

    public ProducerKafkaPortImpl(Producer producerService) {
        this.producerService = producerService;
    }

    @Override
    public void sendToKafka(MessageKafka message) throws IOException {
        producerService.sendToKafka(message);
    }
}
