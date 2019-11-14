package client.actors;

import client.actors.base.Sprite;
import client.event.Event;

public class OtherPlayer extends Sprite {

    public OtherPlayer(int x, int y) {
        super("craft.png", x, y);
    }

    public void move(Event event) {
        x = event.getX();
        y = event.getY();
        angle = event.getAngle();
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
    }
}
