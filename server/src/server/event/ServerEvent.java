package server.event;

public class ServerEvent {
    private ServerEventType type;
    private int senderId = 0;
    private String senderName = "[Server]";
    private int objectId = senderId;
    private int x;
    private int y;
    private double angle;
    private int unitSize;

    public ServerEvent(String eventString) {
        String[] args = eventString.split(" ");
        type = ServerEventType.fromString(args[0]);
        senderId = Integer.parseInt(args[1]);
        senderName = args[2];
        objectId = Integer.parseInt(args[3]);
        x = Integer.parseInt(args[4]);
        y = Integer.parseInt(args[5]);
        angle = Double.parseDouble(args[6]);
        unitSize = Integer.parseInt(args[7]);
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
        return String.format("%s %d %s %d %d %d %f %d", type.name(), senderId, senderName, objectId, x, y, angle, unitSize);
    }
}
