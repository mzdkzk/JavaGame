package client.actors;

import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Item extends Sprite {
    public Item(int x, int y) {
        super(Resources.ITEM, x, y);
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other instanceof Player) {
            Player player = (Player)other;
            if (player.isUser()) {
                // TODO: Eventへの置き換え
                player.addChild(new Unit(player));
            }
            destroy();
        }
    }
}
