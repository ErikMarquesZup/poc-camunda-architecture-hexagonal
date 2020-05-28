package get.camunda.bpm.getstarted.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessInstanceServiceImpl implements ProcessInstanceService{

    @Autowired
    private RuntimeService runtimeService;

    public ProcessInstanceServiceImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void startProcessInstance(String bpmnProcessKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnProcessKey);
        String processInstanceId = processInstance.getProcessInstanceId();
        log.info("started instance: {}", processInstanceId);
    }
}