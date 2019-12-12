package client.actors;

import client.actors.base.CollidableSprite;
import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Unit extends CollidableSprite {
    Player player;

    public Unit(Player player) {
        super(Resources.UNIT);
        this.x = player.getCenterX();
        this.y = player.getCenterY();
        this.angle = player.getAngle();
        this.player = player;
    }

    @Override
    public void update() {
    }

    @Override
    public void onCollisionEnter(Sprite other) {

    }
}
