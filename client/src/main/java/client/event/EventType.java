package client.event;

import client.MyClient;

public enum EventType {
    UPDATE,
    FIRE,
    ITEM,
    ITEM_DELETE,
    DISCONNECT;

    static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.name().equals(type)) {
                return eventType;
            }
        }
        MyClient.showError("存在しないイベントを受け取りました");
        return null;
    }
}
