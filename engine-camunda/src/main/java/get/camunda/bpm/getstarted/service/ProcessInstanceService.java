package get.camunda.bpm.getstarted.service;

import get.camunda.bpm.getstarted.request.JourneyStartRequest;

import java.io.IOException;

public interface ProcessInstanceService {

    String startProcessInstance(JourneyStartRequest startRequest) throws IOException;

    String complete(String id);
}
