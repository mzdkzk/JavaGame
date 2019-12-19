package server.event;

public enum ServerEventType {
    UPDATE,
    FIRE,
    ITEM,
    ITEM_DELETE,
    DISCONNECT;

    static ServerEventType fromString(String type) {
        for (ServerEventType eventType : ServerEventType.values()) {
            if (eventType.name().equals(type)) {
                return eventType;
            }
        }
        System.out.println("存在しないイベントを受け取りました");
        return null;
    }
}
