package br.com.hexagonal.architecture.usecase;

import br.com.hexagonal.architecture.adapter.out.journey.EventManager;
import br.com.hexagonal.architecture.domain.constant.TypeComponent;
import br.com.hexagonal.architecture.domain.model.MessageKafka;
import br.com.hexagonal.architecture.domain.model.StartJourney;
import br.com.hexagonal.architecture.usecase.port.in.journey.ManagementJourneyUseCase;
import br.com.hexagonal.architecture.usecase.port.in.journey.request.JourneyStartRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class ManagementJourneyUseCaseImpl implements ManagementJourneyUseCase {

    @Autowired
    private EventManager eventManager;

//    @Autowired
//    private PersistencePort persistencePort;

//    public ManagementJourneyUseCaseImpl(ProducerKafkaPort kafkaService) {
//        this.kafkaService = kafkaService;
//    }


    public ManagementJourneyUseCaseImpl(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public String execute(JourneyStartRequest requestStart) throws IOException {
        String uuid = UUID.randomUUID().toString();

        MessageKafka message = MessageKafka.builder()
                                           .payload(StartJourney.builder()
                                                   .type(TypeComponent.START_EVENT.getEvent())
                                                   .cpf(requestStart.getCpf())
                                                   .uuid(uuid)
                                                   .bpmnInstance(requestStart.getBpmnInstance()).build())
                                           .topic("command-topics")
                                           .uuid(uuid).build();

        eventManager.sendToKafka(message);

//        log.info(":: 1 - Created instance with id execution {}", uuid);
//        String result = waitingProcessEndByUUID(uuid);
//        log.info(":: 2 - Result of Streams {}", result);

        return "Ok";
    }

//    public String completeTask(String processInstanceId, String taskId) throws IOException {
//        MessageKafka message = MessageKafka.builder()
//                .payload(CompleteTask.builder().taskId(taskId).build())
//                .topic("command-topics").uuid(taskId).build();
//
//        kafkaService.sendToKafka(message);
//
//        log.info(":: 1 - Complete task id {}", taskId);
//
//        String result = waitingProcessEnd(processInstanceId, taskId);
//
//        log.info(":: 2 - Result of Streams {}", result);
//
//        return result;
//    }
//
//    private String waitingProcessEndByUUID(String uuid) throws IOException, FindFailedException {
//        boolean loop = true;
//        Optional<String> result = null;
//        while (loop) {
//            Optional<String> processInstanceId = persistencePort.findByKey(uuid);
//            if (!processInstanceId.isPresent()) {
//                continue;
//            }
//            result = Optional.of(this.waitingProcessEnd(processInstanceId.get()));
//            loop = !result.isPresent();
//        }
//        return result.get();
//    }
//
//    private String waitingProcessEnd(String processInstanceId) throws IOException {
//       return this.waitingProcessEnd(processInstanceId, null);
//    }
//
//    private String waitingProcessEnd(String processInstanceId, String taskId) throws IOException {
//        boolean loop = true;
//        Optional<String> result = null;
//        while (loop) {
//            result = persistencePort.getProcessInstanceInfo(processInstanceId, taskId);
//            loop = !result.isPresent();
//        }
//        return result.get();
//    }
}
