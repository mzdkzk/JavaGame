package client.event;

import client.MyClient;
import client.actors.Player;
import client.game.Game;

public class Event {
    private EventType type;
    private int senderId;
    private int x;
    private int y;
    private double angle;
    public boolean isDone;

    public Event(EventType type) {
        this.type = type;
        this.senderId = MyClient.getUserId();
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

    public Event(String eventString) {
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

    public Player getSender() {
        return Game.joinedPlayers.get(senderId);
    }

    public boolean isOther() {
        return MyClient.getUserId() != senderId;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %f", type.name(), senderId, x, y, angle);
    }
}
