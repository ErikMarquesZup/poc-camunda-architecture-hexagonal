package br.com.hexagonal.architecture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageKafka {

    private String uuid;
    private DomainEvent payload;
    private String topic;
}
