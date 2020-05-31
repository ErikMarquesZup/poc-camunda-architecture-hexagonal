package get.camunda.bpm.getstarted.camunda.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import get.camunda.bpm.getstarted.camunda.constant.TypeComponent;
import get.camunda.bpm.getstarted.camunda.kafka.ProducerService;
import get.camunda.bpm.getstarted.camunda.kafka.model.KafkaMessageInfo;
import lombok.extern.slf4j.Slf4j;
import message.model.*;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.form.handler.TaskFormHandler;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class ProcessEnginePlugin extends AbstractProcessEnginePlugin {

    public static final String EXTENSION_ELEMENTS = "extensionElements";
    public static final String PROPERTIES = "properties";
    public static final String PROPERTY = "property";
    public static final String NAME = "name";
    public static final String KAFKA_TOPIC = "kafkaTopic";
    public static final String SYSTEM_TASK = "systemTask";
    public static final String OPERATION = "operation";
    public static final String VALUE = "value";
    public static final String COMMA = ",";
    public static final String START_EVENT = "start";
    public static final String CREATE = "create";

    public static final String UPDATE_PROPOSAL = "updateProposal";
    public static final String FRAUD = "fraud";
    public static final String STEP_PROCESS = "stepProcess";

    @Autowired
    private ProducerService producerService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProcessEngine engine;
    @Autowired
    private RuntimeService runtimeService;


    public ProcessEnginePlugin() {
    }

    public ProcessEnginePlugin(ProducerService producerService, ObjectMapper objectMapper, ProcessEngine engine, RuntimeService runtimeService) {
        this.producerService = producerService;
        this.objectMapper = objectMapper;
        this.engine = engine;
        this.runtimeService = runtimeService;
    }

    private List<BpmnParseListener> customPreBPMNParseListeners(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        if (processEngineConfiguration.getCustomPreBPMNParseListeners() == null) {
            processEngineConfiguration.setCustomPreBPMNParseListeners(new ArrayList<BpmnParseListener>());
        }
        return processEngineConfiguration.getCustomPreBPMNParseListeners();
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        customPreBPMNParseListeners(processEngineConfiguration)
                .add(new RegisterExternalTaskBpmnParseListener());
    }

    public class RegisterExternalTaskBpmnParseListener extends AbstractBpmnParseListener {

        @Override
        public void parseStartEvent(Element startEvent, ScopeImpl scope, ActivityImpl activity) {
            ActivityBehavior activityBehavior = activity.getActivityBehavior();
            if (activityBehavior instanceof NoneStartEventActivityBehavior) {
                List<String> kafkaTopics = getTopics(startEvent);
                List<String> operations = getProperties(OPERATION, startEvent);

                activity.addListener(START_EVENT, getExecutionListener(kafkaTopics, operations, activity.getId(),TypeComponent.START_EVENT));
            }
        }

        @Override
        public void parseEndEvent(Element endProcess, ScopeImpl scope, ActivityImpl activity) {
            ActivityBehavior activityBehavior = activity.getActivityBehavior();
            if (activityBehavior instanceof NoneEndEventActivityBehavior) {
                List<String> kafkaTopics = getTopics(endProcess);
                List<String> operations = getProperties(OPERATION, endProcess);

                activity.addListener(START_EVENT, getExecutionListener(kafkaTopics, operations, activity.getId(),TypeComponent.END_EVENT));
            }
        }

        @Override
        public void parseUserTask(Element userTask, ScopeImpl scope, ActivityImpl activity) {
            ActivityBehavior activityBehavior = activity.getActivityBehavior();
            if (activityBehavior instanceof UserTaskActivityBehavior) {
                List<String> kafkaTopics = getTopics(userTask);

                boolean isSystemTask = getSystemTask(SYSTEM_TASK, userTask);
                TypeComponent type = isSystemTask ? TypeComponent.SERVICE_TASK_EVENT : TypeComponent.USER_TASK;
                List<String> operations = getProperties(OPERATION, userTask);

                UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
                userTaskActivityBehavior
                        .getTaskDefinition()
                        .addTaskListener(CREATE, getTaskListener(kafkaTopics, operations, activity.getId(), type, isSystemTask));
            }
        }

        private List<String> getTopics(Element element) {
            return ofNullable(element.element(EXTENSION_ELEMENTS))
                    .map(getPropertiesElement())
                    .map(getPropertyList())
                    .map(getFilteredTopicList()).orElse(new ArrayList<>());
        }

        private List<String> getProperties(String propertyName, Element element) {
            return ofNullable(element.element(EXTENSION_ELEMENTS)).map(getPropertiesElement())
                                                                   .map(getPropertyList())
                                                                   .map(getFilteredProperties(propertyName)).orElse(new ArrayList<>());
        }

        private boolean getSystemTask(String propertyName, Element element) {
            return !ofNullable(element.element(EXTENSION_ELEMENTS)).map(getPropertiesElement())
                    .map(getPropertyList())
                    .map(getFilteredProperties(propertyName)).get().isEmpty();
        }

        private TaskListener getTaskListener(List<String> kafkaTopics, List<String> operations, String typeDescription, TypeComponent type, boolean systemTask) {
            return execution -> {
                String processInstanceId = execution.getProcessInstanceId();
                String taskId = execution.getId();
                TaskFormHandler taskFormHandler = ((TaskEntity) execution).getTaskDefinition().getTaskFormHandler();
                TaskFormData taskForm = taskFormHandler.createTaskForm((TaskEntity) execution);
                String uuid = execution.getVariable("uuid") != null ? execution.getVariable("uuid").toString() : "";
                String info = null;
                try {
                    info = objectMapper.writeValueAsString(taskForm.getFormFields()).replace("\\", "");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                logExternalTaskInfo(kafkaTopics, processInstanceId, taskId, info);

                KafkaMessageInfo kafkaMessageInfo = KafkaMessageInfo.builder()
                        .kafkaTopics(kafkaTopics)
                        .messageKafkaOperation(getOperationMessageKafka(operations, execution.getProcessInstanceId(), info, taskId, typeDescription, type, systemTask, uuid))
                        .build();
                producerService.scheduler(kafkaMessageInfo);
            };
        }

        private ExecutionListener getExecutionListener(List<String> kafkaTopics, List<String> operations, String typeDescription, TypeComponent type) {
            return execution -> {
                logExternalTaskInfo(kafkaTopics, execution.getProcessInstanceId());

                String uuid = execution.getVariable("uuid") != null ? execution.getVariable("uuid").toString() : "";

                KafkaMessageInfo kafkaMessageInfo = KafkaMessageInfo.builder()
                        .kafkaTopics(kafkaTopics)
                        .messageKafkaOperation(getOperationMessageKafka(operations, execution.getProcessInstanceId(), null, execution.getActivityInstanceId(),
                                                                        execution.getCurrentActivityId(), execution.getId(), typeDescription, type, true, uuid))
                        .build();


                producerService.scheduler(kafkaMessageInfo);
            };
        }

        private void logExternalTaskInfo(List<String> topics, String processInstanceId) {
            logExternalTaskInfo(topics, processInstanceId, null, null);
        }

        private void logExternalTaskInfo(List<String> topics, String processInstanceId, String taskId, Object info) {
            log.info("processInstanceId: {}", processInstanceId);
            log.info("taskId: [{}]", taskId);
            log.info("kafkaTopcis: [{}]", getKafkaTopicList(topics));
            log.info("info: [{}]", info);
        }

        private String getKafkaTopicList(List<String> topics) {
            if (CollectionUtils.isEmpty(topics)) return StringUtils.EMPTY;
            StringBuilder topicsName = new StringBuilder();
            topics.forEach(s -> topicsName.append(s).append(COMMA));
            topicsName.deleteCharAt(topicsName.length() - 1);
            return topicsName.toString();
        }

        private Function<List<Element>, List<String>> getFilteredTopicList() {
            return propertyList ->
                    propertyList
                            .stream()
                            .filter(getTopicNamePredicate())
                            .map(getValue()).collect(Collectors.toList());
        }

        private Function<List<Element>, List<String>> getFilteredProperties(String propertyName) {
            return propertyList ->
                    propertyList
                            .stream()
                            .filter(getPropertyNamePredicate(propertyName))
                            .map(getValue()).collect(Collectors.toList());
        }

        private Function<Element, String> getValue() {
            return property -> property.attribute(VALUE);
        }

        private Predicate<Element> getPropertyNamePredicate(String propertyName) {
            return property -> propertyName.equals(property.attribute(NAME));
        }

        private Predicate<Element> getTopicNamePredicate() {
            return property -> StringUtils.startsWith(property.attribute(NAME), KAFKA_TOPIC);
        }

        private Function<Element, List<Element>> getPropertyList() {
            return propertiesElement -> propertiesElement.elements(PROPERTY);
        }

        private Function<Element, Element> getPropertiesElement() {
            return extensionElement -> extensionElement.element(PROPERTIES);
        }
    }

    private Map<String, MessageKafka> getOperationMessageKafka(List<String> operations, String processInstanceId, String info, String taskId, String typeDescription, TypeComponent type, Boolean systemTask, String uuid) {
        return this.getOperationMessageKafka(operations, processInstanceId, info, null, null, taskId, typeDescription, type, systemTask, uuid);
    }

    private Map<String, MessageKafka> getOperationMessageKafka(List<String> operations, String processInstanceId, String info, String activityInstanceId, String currentActivityId,
                                                               String taskId, String typeDescription, TypeComponent type, boolean systemTask, String uuid) {
        Map<String, MessageKafka> messageKafkaMap = new HashMap<>();
        operations.stream().forEach(operation -> messageKafkaMap.put(operation, getMessageKafka(operation, processInstanceId, info, activityInstanceId, currentActivityId, taskId,  typeDescription, type, systemTask, uuid)));
        return messageKafkaMap;
    }

    private MessageKafka getMessageKafka(String operation, String processInstanceId, String info, String activityInstanceId, String currentActivityId,
                                         String taskId, String typeDescription, TypeComponent type, boolean systemTask, String uuid) {
        MessageKafka messageKafka = MessageKafka.builder().build();
        if (STEP_PROCESS.equals(operation)) {
                messageKafka.setPayload(StepProcess.builder().processInstanceId(processInstanceId)
                        .processInstanceId(processInstanceId)
                        .taskId(taskId)
                        .uuid(uuid.isEmpty() ? processInstanceId : uuid)
                        .infoUserTask(info)
                        .systemTask(systemTask)
                        .typeOperation(STEP_PROCESS)
                        .typeDescription(typeDescription)
                        .type(type.getEvent()).build());
        } else if (TypeComponent.SERVICE_TASK_EVENT.equals(type)) {
            if (UPDATE_PROPOSAL.equals(operation)) {
                messageKafka.setPayload(UpdateProposal.builder()
                        .processInstanceId(processInstanceId)
                        .taskId(taskId)
                        .uuid(uuid.isEmpty() ? processInstanceId : uuid)
                        .infoUserTask(info)
                        .systemTask(systemTask)
                        .typeDescription(typeDescription)
                        .type(type.getEvent()).build());

            } else if (FRAUD.equals(operation)) {
                messageKafka.setPayload(Fraud.builder()
                        .processInstanceId(processInstanceId)
                        .taskId(taskId)
                        .uuid(uuid.isEmpty() ? processInstanceId : uuid)
                        .infoUserTask(info)
                        .systemTask(systemTask)
                        .typeDescription(typeDescription)
                        .type(type.getEvent()).build());
            }
        }
        return messageKafka;
    }
}
