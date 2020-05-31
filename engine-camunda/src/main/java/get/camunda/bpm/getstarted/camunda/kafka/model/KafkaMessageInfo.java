package get.camunda.bpm.getstarted.camunda.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import message.model.MessageKafka;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class KafkaMessageInfo {

    private List<String> kafkaTopics;

    private Map<String, MessageKafka> messageKafkaOperation;

}
