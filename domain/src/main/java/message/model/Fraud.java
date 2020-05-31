package message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Fraud extends DomainEvent {
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
}
