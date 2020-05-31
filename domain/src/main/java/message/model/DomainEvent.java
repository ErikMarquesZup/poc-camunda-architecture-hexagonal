package message.model;

public class DomainEvent {
    private String uuid;

    public DomainEvent(String uuid) {
        this.uuid = uuid;
    }
    public DomainEvent() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
