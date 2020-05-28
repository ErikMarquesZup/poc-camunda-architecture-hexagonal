package get.camunda.bpm.getstarted.service;

import java.io.IOException;

public interface ProcessInstanceService {

    void startProcessInstance(String bpmnProcessKey) throws IOException;
}
