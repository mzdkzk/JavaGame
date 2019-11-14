package client;

enum EventType {
    join,
    move,
    delete;

    static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.name().equals(type)) {
                return eventType;
            }
        }
        return null;
    }
}

public class Event {
    private EventType type;
    private int senderId;
    private int x;
    private int y;
    private double angle;

    Event(EventType type, int x, int y, double angle) {
        this.type = type;
        this.senderId = MyGameClient.userId;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    Event(String eventString) {
        String[] args = eventString.split(" ");
        type = EventType.fromString(args[0]);
        senderId = Integer.parseInt(args[1]);
        x = Integer.parseInt(args[2]);
        y = Integer.parseInt(args[3]);
        angle = Double.parseDouble(args[4]);
    }

    public EventType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSenderId() {
        return senderId;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %f", type.name(), senderId, x, y, angle);
    }
}
