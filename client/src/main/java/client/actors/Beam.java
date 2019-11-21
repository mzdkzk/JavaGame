package client.actors;

import client.actors.base.Sprite;

public class Beam extends Sprite {
    private double moveSpeed = 10.0;

    public Beam(Player from) {
        super("missile.png",
                from.getX() + from.getWidth() / 2 + (int)(Math.cos(from.getAngle()) * 10),
                from.getY() + from.getHeight() / 2 + (int)(Math.sin(from.getAngle()) * 10)
        );
        angle = from.getAngle();
    }

    @Override
    public void update() {
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
