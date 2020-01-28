package client.actors;

import client.actors.base.Hittable;
import client.actors.base.Sprite;
import client.game.Game;
import client.game.resource.Resources;

public class Beam extends Sprite {
    private Sprite from;
    private int lifespan = 60;

    public Beam(Sprite from) {
        super(Resources.BEAM);
        int fireOffset = 30;
        x = from.getCenterX() + (int)(Math.cos(from.getAngle()) * fireOffset) - getWidth() / 2;
        y = from.getCenterY() + (int)(Math.sin(from.getAngle()) * fireOffset) - getHeight() / 2;
        angle = from.getAngle();
        this.from = from;
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
        if (!(other instanceof Hittable)) return;

        // fromとfrom.getParent()から発射したプレイヤーを特定し、
        // 接触相手がプレイヤーかプレイヤーのユニットでなければヒット
        Player player = from instanceof Player ? (Player)from : (Player)from.getParent();
        if (other != player && other.getParent() != player) {
            ((Hittable)other).hit(1);
            Game.getRoot().addChild(new Bomb(x, y));
            destroy();
        }
    }
}
