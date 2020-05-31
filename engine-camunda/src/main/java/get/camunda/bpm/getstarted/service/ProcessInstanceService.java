package get.camunda.bpm.getstarted.service;

import get.camunda.bpm.getstarted.request.JourneyStartRequest;

import java.io.IOException;

public interface ProcessInstanceService {

    void startProcessInstance(JourneyStartRequest startRequest) throws IOException;
}
