package get.camunda.bpm.getstarted.camunda.kafka;

import get.camunda.bpm.getstarted.camunda.kafka.model.KafkaMessageInfo;
import message.model.DomainEvent;
import message.model.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, DomainEvent> producerKafka;

    public ProducerService(KafkaTemplate<String, DomainEvent> producerKafka) {
        this.producerKafka = producerKafka;
    }

    public void scheduler(KafkaMessageInfo kafkaMessageInfo) {
        if (kafkaMessageInfo.getKafkaTopics().isEmpty()) {
            return;
        }
        kafkaMessageInfo.getKafkaTopics().stream().forEach(topic -> sendToKafka(topic, getMessageKafka(kafkaMessageInfo.getMessageKafkaOperation())));
    }

    private List<MessageKafka> getMessageKafka(Map<String, MessageKafka> messageKafkaOperation) {
        List<MessageKafka> messages = new ArrayList<>();
        messageKafkaOperation.forEach((key, value) -> messages.add(value));
        return messages;
    }

    private void sendToKafka(String topic, List<MessageKafka> messageKafkaOperation) {
        messageKafkaOperation.stream().forEach(
                message -> producerKafka.send(topic, message.getPayload().getUuid(), message.getPayload())
        );
    }
}
