package br.com.hexagonal.architecture.journey.consumer;

import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import br.com.hexagonal.architecture.journey.service.ConsumerService;
import br.com.hexagonal.architecture.journey.service.EngineClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import javax.inject.Named;
import java.io.IOException;
import java.util.Collections;

@Named
@KafkaListener(topics = {"command-topics"})
@Slf4j
public class EventConsumerImpl implements EventConsumer {

    private static String topic = "command-topics";

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @KafkaHandler
    public void consume(StartJourney startJourney) throws IOException, SaveFailedException {
        log.info(":: Consuming Start Process :: UUID {}, CPF {}, BPMN {}", startJourney.getUuid(), startJourney.getCpf(), startJourney.getBpmnInstance());
        String processInstanceId = consumerService.start(JourneyStartRequest.builder().bpmnInstance(startJourney.getBpmnInstance()).uuid(startJourney.getUuid()).build());
        consumerService.save(startJourney.getUuid(), processInstanceId);
    }

    @Override
    @KafkaHandler
    public void consume(UpdateProposal updateProposal) throws IOException {
        log.info(":: Consuming Update Proposal :: UUID {}, CPF {}, BPMN {}", updateProposal.getUuid(), updateProposal.getCpf(), updateProposal.getBpmnInstance());
        updateProposal.setTypeOperation("CompleteTask");
        consumerService.sendToKafka(new MessageKafka(updateProposal.getUuid(), new CompleteTask(updateProposal), topic));
    }

    @Override
    @KafkaHandler
    public void consume(StepProcess stepProcess) throws IOException, SaveFailedException {
        log.info(":: Consuming Step Process :: UUID {}, CPF {}, BPMN {}", stepProcess.getUuid(), stepProcess.getCpf(), stepProcess.getBpmnInstance());
        DomainEventList domainEvents = DomainEventList.builder().domainEvents(Collections.singletonList(stepProcess)).build();
        consumerService.save(stepProcess.getProcessInstanceId(), objectMapper.writeValueAsString(domainEvents));
    }

    @Override
    @KafkaHandler
    public void consume(Fraud fraud) throws IOException {
        log.info(":: Consuming Fraud :: UUID {}, CPF {}, BPMN {}", fraud.getUuid(), fraud.getCpf(), fraud.getBpmnInstance());
        fraud.setTypeOperation("CompleteTask");
        consumerService.sendToKafka(new MessageKafka(fraud.getUuid(), new CompleteTask(fraud), topic));
    }

    @Override
    @KafkaHandler
    public void consume(CompleteTask completeTask) throws InterruptedException {
        log.info(":: Consuming Complete Task :: UUID {}, CPF {}, BPMN {}", completeTask.getUuid(), completeTask.getCpf(), completeTask.getBpmnInstance());
        consumerService.complete(completeTask.getTaskId());
    }
}
