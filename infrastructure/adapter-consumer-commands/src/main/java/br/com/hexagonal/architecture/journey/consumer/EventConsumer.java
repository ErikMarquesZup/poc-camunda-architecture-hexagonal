package br.com.hexagonal.architecture.journey.consumer;

import message.model.*;

import java.io.IOException;

public interface EventConsumer {

    void consume(StartJourney startJourney) throws IOException;

    void consume(UpdateProposal updateProposal) throws InterruptedException;

    void consume(StepProcess stepProcess) throws InterruptedException;

    void consume(Fraud fraud) throws InterruptedException;

}
