package get.camunda.bpm.getstarted.service;

import get.camunda.bpm.getstarted.request.JourneyStartRequest;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ProcessInstanceServiceImpl implements ProcessInstanceService{

    @Autowired
    private RuntimeService runtimeService;

    public ProcessInstanceServiceImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void startProcessInstance(JourneyStartRequest startRequest) {

        Map<String, Object> variables=new HashMap<>();
        variables.put("uuid",startRequest.getUuid());

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService=processEngine.getRuntimeService();

        ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey(startRequest.getBpmnInstance())
                .setVariables(variables)
                .executeWithVariablesInReturn();

        String processInstanceId = instance.getProcessInstanceId();

        log.info("started instance: {}", processInstanceId);
    }
}