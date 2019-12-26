package client.event;

import client.MyClient;
import client.actors.Player;
import client.game.Game;

public class Event {
    private EventType type;
    private int senderId;
    private int objectId;
    private String senderName;
    private int x;
    private int y;
    private double angle;
    private int unitSize;
    public boolean isDone;

    public Event(EventType type) {
        this.type = type;
        this.senderId = MyClient.getUserId();
        this.senderName = MyClient.getUserName();
        this.objectId = this.senderId;
    }

    public Event(EventType type, int x, int y) {
        this(type);
        this.x = x;
        this.y = y;
    }

    public Event(EventType type, int x, int y, double angle) {
        this(type, x, y);
        this.angle = angle;
    }

    public Event(EventType type, int x, int y, double angle, int unitSize) {
        this(type, x, y, angle);
        this.unitSize = unitSize;
    }

    public Event(String eventString) {
        String[] args = eventString.split(" ");
        type = EventType.fromString(args[0]);
        senderId = Integer.parseInt(args[1]);
        senderName = args[2];
        objectId = Integer.parseInt(args[3]);
        x = Integer.parseInt(args[4]);
        y = Integer.parseInt(args[5]);
        angle = Double.parseDouble(args[6]);
        unitSize = Integer.parseInt(args[7]);
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

    public String getSenderName() {
        return senderName;
    }

    public int getObjectId() {
        return objectId;
    }

    public Event with(int objectId) {
        this.objectId = objectId;
        return this;
    }

    public double getAngle() {
        return angle;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public Player getSender() {
        return Game.joinedPlayers.get(senderId);
    }

    public boolean isOther() {
        return MyClient.getUserId() != senderId;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s %d %d %d %f %d", type.name(), senderId, senderName, objectId, x, y, angle, unitSize);
    }
}
