package br.com.hexagonal.architecture.journey.service;

import br.com.application.ports.PersistencePort;
import br.com.application.ports.ProducerKafkaPort;
import br.com.hexagonal.architecture.journey.constants.TypeComponent;
import br.com.hexagonal.architecture.journey.request.JourneyStartRequest;
import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.FindFailedException;
import lombok.extern.slf4j.Slf4j;
import message.model.CompleteTask;
import message.model.MessageKafka;
import message.model.StartJourney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class StartJourneyService {

    @Autowired
    private ProducerKafkaPort kafkaService;

    @Autowired
    private PersistencePort persistencePort;

    public StartJourneyService(ProducerKafkaPort kafkaService) {
        this.kafkaService = kafkaService;
    }

    public String start(JourneyStartRequest requestStart) throws IOException, FindFailedException {
        String uuid = UUID.randomUUID().toString();

        MessageKafka message = MessageKafka.builder()
                                           .payload(StartJourney.builder()
                                                   .type(TypeComponent.START_EVENT.getEvent())
                                                   .cpf(requestStart.getCpf())
                                                   .uuid(uuid)
                                                   .bpmnInstance(requestStart.getBpmnInstance()).build())
                                           .topic("command-topics")
                                           .uuid(uuid).build();

        kafkaService.sendToKafka(message);

        log.info(":: 1 - Created instance with id execution {}", uuid);
        String result = waitingProcessEndByUUID(uuid);
        log.info(":: 2 - Result of Streams {}", result);

        return null;
    }

    public String completeTask(String processInstanceId, String taskId) throws IOException {
        MessageKafka message = MessageKafka.builder()
                .payload(CompleteTask.builder().taskId(taskId).build())
                .topic("command-topics").uuid(taskId).build();

        kafkaService.sendToKafka(message);

        log.info(":: 1 - Complete task id {}", taskId);

        String result = waitingProcessEnd(processInstanceId, taskId);

        log.info(":: 2 - Result of Streams {}", result);

        return result;
    }

    private String waitingProcessEndByUUID(String uuid) throws IOException, FindFailedException {
        boolean loop = true;
        Optional<String> result = null;
        while (loop) {
            Optional<String> processInstanceId = persistencePort.findByKey(uuid);
            if (!processInstanceId.isPresent()) {
                continue;
            }
            result = Optional.of(this.waitingProcessEnd(processInstanceId.get()));
            loop = !result.isPresent();
        }
        return result.get();
    }

    private String waitingProcessEnd(String processInstanceId) throws IOException {
       return this.waitingProcessEnd(processInstanceId, null);
    }

    private String waitingProcessEnd(String processInstanceId, String taskId) throws IOException {
        boolean loop = true;
        Optional<String> result = null;
        while (loop) {
            result = persistencePort.getProcessInstanceInfo(processInstanceId, taskId);
            loop = !result.isPresent();
        }
        return result.get();
    }
}
