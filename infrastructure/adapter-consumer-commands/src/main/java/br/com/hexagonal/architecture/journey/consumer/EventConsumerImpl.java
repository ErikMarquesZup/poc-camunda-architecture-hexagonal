package br.com.hexagonal.architecture.journey.consumer;

import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.service.EngineClient;
import lombok.extern.slf4j.Slf4j;
import message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

import javax.inject.Named;
import java.io.IOException;

@Named
@KafkaListener(topics = {"command-topics"})
@Slf4j
public class EventConsumerImpl implements EventConsumer {

    @Autowired
    private EngineClient engineClient;

    @Override
    @KafkaHandler
    public void consume(StartJourney startJourney) throws IOException {
        log.info(":: Consuming Start Process :: UUID {}, CPF {}, BPMN {}", startJourney.getUuid(), startJourney.getCpf(), startJourney.getBpmnInstance());
        engineClient.start(JourneyStartRequest.builder().bpmnInstance(startJourney.getBpmnInstance()).uuid(startJourney.getUuid()).build());
    }

    @Override
    @KafkaHandler
    public void consume(UpdateProposal updateProposal) throws InterruptedException {
        log.info(":: Consuming Update Proposal :: UUID {}, CPF {}, BPMN {}", updateProposal.getUuid(), updateProposal.getCpf(), updateProposal.getBpmnInstance());
        Thread.sleep(5000);
    }

    @Override
    @KafkaHandler
    public void consume(StepProcess stepProcess) throws InterruptedException {
        log.info(":: Consuming Step Process :: UUID {}, CPF {}, BPMN {}", stepProcess.getUuid(), stepProcess.getCpf(), stepProcess.getBpmnInstance());
        Thread.sleep(5000);
    }

    @Override
    @KafkaHandler
    public void consume(Fraud fraud) throws InterruptedException {
        log.info(":: Consuming Fraud :: UUID {}, CPF {}, BPMN {}", fraud.getUuid(), fraud.getCpf(), fraud.getBpmnInstance());
        Thread.sleep(5000);
    }
}
