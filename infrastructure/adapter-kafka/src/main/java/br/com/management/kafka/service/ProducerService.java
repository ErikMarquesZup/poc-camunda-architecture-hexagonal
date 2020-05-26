package br.com.management.kafka.service;

import br.com.management.kafka.message.MessageKafka;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ProducerService {

    void sendToKafka(Message<MessageKafka> kafkaExternalTaskMessage);

    MessageKafka sendToKafkaReply(Message<MessageKafka> kafkaExternalTaskMessage)  throws ExecutionException, InterruptedException, IOException;
}
