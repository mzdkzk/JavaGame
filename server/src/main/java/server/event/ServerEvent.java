package server.event;

public class ServerEvent {
    private ServerEventType type;
    private int senderId = 0;
    private int objectId = senderId;
    private int x;
    private int y;
    private double angle;
    private int unitSize;

    public ServerEvent(String eventString) {
        String[] args = eventString.split(" ");
        type = ServerEventType.fromString(args[0]);
        senderId = Integer.parseInt(args[1]);
        objectId = Integer.parseInt(args[2]);
        x = Integer.parseInt(args[3]);
        y = Integer.parseInt(args[4]);
        angle = Double.parseDouble(args[5]);
        unitSize = Integer.parseInt(args[6]);
    }

    public ServerEvent(ServerEventType type) {
        this.type = type;
    }

    public ServerEvent(ServerEventType type, int x, int y) {
        this(type);
        this.x = x;
        this.y = y;
    }

    public ServerEvent(ServerEventType type, int x, int y, double angle) {
        this(type, x, y);
        this.angle = angle;
    }

    public ServerEvent with(int objectId) {
        this.objectId = objectId;
        return this;
    }

    public ServerEventType getType() {
        return type;
    }

    public int getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %d %f %d", type.name(), senderId, objectId, x, y, angle, unitSize);
    }
}
