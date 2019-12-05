package client.actors;

import client.actors.base.CollidableSprite;
import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Beam extends CollidableSprite {
    private Player fromPlayer;
    private int lifespan = 60;

    public Beam(Player from) {
        super(Resources.BEAM);
        int fireOffset = 30;
        x = from.getX() + from.getWidth() / 2 + (int)(Math.cos(from.getAngle()) * fireOffset) - getWidth() / 2;
        y = from.getY() + from.getHeight() / 2 + (int)(Math.sin(from.getAngle()) * fireOffset) - getHeight() / 2;
        angle = from.getAngle();
        fromPlayer = from;
    }

    @Override
    public void update() {
        if (lifespan <= 0) {
            destroy();
        }
        lifespan--;

        double moveSpeed = 10.0;
        x += Math.cos(angle) * moveSpeed;
        y += Math.sin(angle) * moveSpeed;
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other != fromPlayer && other instanceof Player) {
            ((Player)other).hit(1);
            destroy();
        }
    }
}
