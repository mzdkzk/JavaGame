package client.actors;

import client.MyClient;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
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
                MyClient.send(new Event(EventType.UNIT));
            }
            destroy();
        }
    }
}
