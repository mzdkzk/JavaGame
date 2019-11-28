package client.actors;

import client.actors.base.Sprite;

public class Beam extends Sprite {

    public Beam(Player from) {
        super("beam.png");
        int fireOffset = 15;
        x = from.getX() + from.getWidth() / 2 + (int)(Math.cos(from.getAngle()) * fireOffset) - getWidth() / 2;
        y = from.getY() + from.getHeight() / 2 + (int)(Math.sin(from.getAngle()) * fireOffset) - getHeight() / 2;
        angle = from.getAngle();
    }

    @Override
    public void update() {
        double moveSpeed = 20.0;
        this.x += Math.cos(angle) * moveSpeed;
        this.y += Math.sin(angle) * moveSpeed;
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other instanceof OtherPlayer) {
            System.out.println("あたった");
        }
    }
}
