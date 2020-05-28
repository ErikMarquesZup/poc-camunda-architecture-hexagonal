package br.com.management.kafka.message;

public class DomainEvent {

    public DomainEvent(String uuid) {
        this.uuid = uuid;
    }

    public DomainEvent() {
    }

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
