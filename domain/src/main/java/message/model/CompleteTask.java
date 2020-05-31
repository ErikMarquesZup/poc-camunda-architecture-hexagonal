package message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompleteTask extends DomainEvent {
    private String uuid;
    private String type;
    private String typeOperation;
    private String cpf;
    private String typeDescription;
    private boolean systemTask = true;
    private String bpmnInstance;
    private String processInstanceId;
    private String taskId;
    private boolean taskComplete;
    private String activityInstanceId;
    private String currentActivityId;
    private Object infoUserTask;

    public CompleteTask(UpdateProposal updateProposal) {
        this.uuid = updateProposal.getUuid();
        this.type = updateProposal.getType();
        this.typeOperation = updateProposal.getTypeOperation();
        this.cpf = updateProposal.getCpf();
        this.typeDescription = updateProposal.getTypeDescription();
        this.systemTask = updateProposal.isSystemTask();
        this.bpmnInstance = updateProposal.getBpmnInstance();
        this.processInstanceId  = updateProposal.getProcessInstanceId();
        this.taskId = updateProposal.getTaskId();
        this.taskComplete = updateProposal.isTaskComplete();
        this.activityInstanceId = updateProposal.getActivityInstanceId();
        this.currentActivityId = updateProposal.getCurrentActivityId();
        this.infoUserTask = updateProposal.getInfoUserTask();
    }

    public CompleteTask(Fraud fraud) {
        this.uuid = fraud.getUuid();
        this.type = fraud.getType();
        this.typeOperation = fraud.getTypeOperation();
        this.cpf = fraud.getCpf();
        this.typeDescription = fraud.getTypeDescription();
        this.systemTask = fraud.isSystemTask();
        this.bpmnInstance = fraud.getBpmnInstance();
        this.processInstanceId  = fraud.getProcessInstanceId();
        this.taskId = fraud.getTaskId();
        this.taskComplete = fraud.isTaskComplete();
        this.activityInstanceId = fraud.getActivityInstanceId();
        this.currentActivityId = fraud.getCurrentActivityId();
        this.infoUserTask = fraud.getInfoUserTask();
    }
}
