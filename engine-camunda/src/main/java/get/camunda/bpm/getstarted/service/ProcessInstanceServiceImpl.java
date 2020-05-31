package get.camunda.bpm.getstarted.service;

import get.camunda.bpm.getstarted.request.JourneyStartRequest;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ProcessInstanceServiceImpl implements ProcessInstanceService{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public ProcessInstanceServiceImpl(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public String startProcessInstance(JourneyStartRequest startRequest) {

        Map<String, Object> variables=new HashMap<>();
        variables.put("uuid",startRequest.getUuid());

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService=processEngine.getRuntimeService();

        ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey(startRequest.getBpmnInstance())
                .setVariables(variables)
                .executeWithVariablesInReturn();

        String processInstanceId = instance.getProcessInstanceId();

        log.info(":: Started instance: {}", processInstanceId);

        return processInstanceId;
    }

    @Override
    public String complete(String id) {
        Optional<Task> task = Optional.of(taskService.createTaskQuery().taskId(id).singleResult());
        if (!task.isPresent()) {
            throw new TaskRejectedException("Task id not Found!");
        }
        taskService.complete(id);
        log.info(":: Completing Task Id{}", id);
        return task.get().getProcessInstanceId();
    }
}