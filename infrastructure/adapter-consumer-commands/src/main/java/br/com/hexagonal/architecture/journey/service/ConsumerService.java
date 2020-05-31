package br.com.hexagonal.architecture.journey.service;

import br.com.application.ports.PersistencePort;
import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import message.model.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsumerService {

    @Autowired
    private EngineClient engineClient;

    @Autowired
    private ProducerKafkaPort producerKafkaPort;

    @Autowired
    private PersistencePort persistencePort;

    public ConsumerService(EngineClient engineClient, ProducerKafkaPort producerKafkaPort) {
        this.engineClient = engineClient;
        this.producerKafkaPort = producerKafkaPort;
    }

    public String start(JourneyStartRequest journeyStartRequest) throws IOException {
        return engineClient.start(journeyStartRequest).getBody();
    }

    public void complete(String taskId) {
        engineClient.complete(taskId);
    }

    public void sendToKafka(MessageKafka messageKafka) throws IOException {
        producerKafkaPort.sendToKafka(messageKafka);
    }

    public void save(String key, String value) throws IOException, SaveFailedException {
        persistencePort.save(key, value);
    }

}
