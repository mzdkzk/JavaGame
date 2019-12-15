package server;

public class ServerEvent {
    private String type;
    private int senderId = 101;
    private int x;
    private int y;
    private double angle;

    public ServerEvent(String type) {
        this.type = type;
    }

    public ServerEvent(String type, int x, int y) {
        this(type);
        this.x = x;
        this.y = y;
    }

    public ServerEvent(String type, int x, int y, double angle) {
        this(type, x, y);
        this.angle = angle;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %f 0", type, senderId, x, y, angle);
    }
}
