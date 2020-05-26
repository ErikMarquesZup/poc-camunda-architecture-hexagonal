package br.com.application.ports;

import br.com.management.kafka.constants.TypeComponent;
import br.com.management.kafka.message.MessageKafka;
import br.com.management.kafka.service.ProducerService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

public class ProducerKafkaPortImpl implements ProducerKafkaPort {

    private ProducerService producerService;

    public ProducerKafkaPortImpl(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void sendToKafka(MessageKafka messageKafka) {
        String uuid = UUID.randomUUID().toString();
        Message<MessageKafka> message = MessageBuilder
                .withPayload(MessageKafka.builder()
                        .type(TypeComponent.START_EVENT.getEvent())
                        .cpf(messageKafka.getCpf())
                        .uuid(uuid)
                        .internalUserTask(Boolean.TRUE)
                        .bpmnInstance(messageKafka.getBpmnInstance()).uuid(uuid).build())
                .setHeader(KafkaHeaders.TOPIC, "start-process")
                .build();
        producerService.sendToKafka(message);
    }
}
