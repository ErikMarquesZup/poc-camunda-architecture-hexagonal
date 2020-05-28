package br.com.management.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageKafka {

    private Class clazz;
    private String uuid;
    private DomainEvent payload;
    private String topic;
}
