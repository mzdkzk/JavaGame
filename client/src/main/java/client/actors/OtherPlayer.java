package client.actors;

import client.event.Event;

public class OtherPlayer extends Player {
    private Event event;

    public OtherPlayer(Event event) {
        super(event.getX(), event.getY());
    }

    public void setEvent(Event event) {
        this.event = event;
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
