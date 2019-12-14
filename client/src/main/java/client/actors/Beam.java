package client.actors;

import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Beam extends Sprite {
    private Sprite player;
    private int lifespan = 60;

    public Beam(Sprite player) {
        super(Resources.BEAM);
        int fireOffset = 30;
        x = player.getCenterX() + (int)(Math.cos(player.getAngle()) * fireOffset) - getWidth() / 2;
        y = player.getCenterY() + (int)(Math.sin(player.getAngle()) * fireOffset) - getHeight() / 2;
        angle = player.getAngle();
        this.player = player;
    }

    @Override
    public void update() {
        if (lifespan <= 0) {
            destroy();
        }
        lifespan--;

        double moveSpeed = 20.0;
        x += Math.cos(angle) * moveSpeed;
        y += Math.sin(angle) * moveSpeed;
    }

    @Override
    public void onCollisionEnter(Sprite other) {

    }
}
