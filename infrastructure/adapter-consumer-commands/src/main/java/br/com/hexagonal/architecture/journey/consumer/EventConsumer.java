package br.com.hexagonal.architecture.journey.consumer;

import br.com.hexagonal.architecture.journey.rocksdb.kv.exception.SaveFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import message.model.*;
import org.springframework.kafka.annotation.KafkaHandler;

import java.io.IOException;

public interface EventConsumer {

    void consume(StartJourney startJourney) throws IOException, SaveFailedException;

    void consume(UpdateProposal updateProposal) throws InterruptedException, IOException;

    void consume(StepProcess stepProcess) throws InterruptedException, IOException, SaveFailedException;

    void consume(Fraud fraud) throws InterruptedException, IOException;

    void consume(CompleteTask completeTask) throws InterruptedException;
}
