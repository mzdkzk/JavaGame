package client.actors;

import client.event.Event;

public class OtherPlayer extends Player {
    public Event event;

    public OtherPlayer(Event event) {
        super(event.getX(), event.getY());
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
