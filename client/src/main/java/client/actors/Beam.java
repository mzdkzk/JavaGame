package client.actors;

import client.actors.base.Sprite;

public class Beam extends Sprite {
    private double moveSpeed = 30.0;

    public Beam(Player from) {
        super("missile.png",
                from.getX() + from.getWidth() / 2,
                from.getY() + from.getHeight() / 2
        );
        angle = from.getAngle();
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        this.x += Math.cos(angle) * moveSpeed;
        this.y += Math.sin(angle) * moveSpeed;
    }
}
