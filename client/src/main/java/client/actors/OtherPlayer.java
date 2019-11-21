package client.actors;

import client.event.Event;

public class OtherPlayer extends Player {
    public OtherPlayer(Event event) {
        super(event);
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        x = event.getX();
        y = event.getY();
        angle = event.getAngle();
    }
}
