package client.actors;

import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Bomb extends Sprite {
    int lifeSpan = 3;

    public Bomb(int x, int y) {
        super(Resources.BOMB, x, y);
    }

    @Override
    public void update() {
        if (lifeSpan > 0) {
            lifeSpan--;
        } else {
            destroy();
        }
    }
}
