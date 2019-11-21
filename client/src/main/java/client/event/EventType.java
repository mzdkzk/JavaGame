package client.event;

public enum EventType {
    UPDATE,
    FIRE,
    DISCONNECT;

    static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.name().equals(type)) {
                return eventType;
            }
        }
        return null;
    }
}
