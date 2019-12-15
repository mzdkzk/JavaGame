package client.actors;

import client.actors.base.Sprite;
import client.event.Event;
import client.game.resource.Resources;

public class Item extends Sprite {
    public Item(Event event) {
        super(Resources.ITEM, event.getX(), event.getY());
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other instanceof Player) {
            Player player = (Player)other;
            if (player.isUser()) {
                player.addChild(new Unit(player));
            }
            destroy();
        }
    }
}
