package br.com.management.kafka;

import br.com.management.kafka.message.DomainEvent;
import br.com.management.kafka.message.MessageKafka;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class ProducerImpl implements Producer {

    @Autowired
    private KafkaTemplate<String, DomainEvent> producerKafka;

    public ProducerImpl(KafkaTemplate<String, DomainEvent> template) {
        this.producerKafka = template;
    }

    @Async
    public void sendToKafka(MessageKafka message) {
        ListenableFuture<SendResult<String, DomainEvent>> future = producerKafka.send(message.getTopic(), message.getPayload().getUuid(), message.getPayload());
        future.addCallback(new ListenableFutureCallback<SendResult<String, DomainEvent>>() {

            @Override
            public void onSuccess(SendResult<String, DomainEvent> result) {
                log.info("Success :: " + ToStringBuilder.reflectionToString(result.toString()));
            }

            @Override
            public void onFailure(Throwable exception) {
                log.error(exception.getMessage());
            }
        });
    }
}


