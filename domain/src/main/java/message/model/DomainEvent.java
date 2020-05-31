package message.model;

import lombok.Data;

@Data
public class DomainEvent {
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

    public DomainEvent(String uuid) {
        this.uuid = uuid;
    }

    public DomainEvent() {

    }
}
