package client.actors;

import client.actors.base.CollidableSprite;
import client.actors.base.Sprite;
import client.game.Game;

public class Beam extends CollidableSprite {

    public Beam(Player from) {
        super("beam.png");
        int fireOffset = 30;
        x = from.getX() + from.getWidth() / 2 + (int)(Math.cos(from.getAngle()) * fireOffset) - getWidth() / 2;
        y = from.getY() + from.getHeight() / 2 + (int)(Math.sin(from.getAngle()) * fireOffset) - getHeight() / 2;
        angle = from.getAngle();
    }

    @Override
    public void update() {
        double moveSpeed = 20.0;
        x += Math.cos(angle) * moveSpeed;
        y += Math.sin(angle) * moveSpeed;

        if (Game.stage.getWidth() / 2 <= Math.abs(x) || Game.stage.getHeight() / 2 <= Math.abs(y)) {
            Game.removeChild(this);
        }
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other instanceof Player) {
            Game.removeChild(this);
        }
    }
}
