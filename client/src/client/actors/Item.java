package client.actors;

import client.MyClient;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.resource.Resources;

public class Item extends Sprite {
    private int itemId;

    public Item(Event event) {
        super(Resources.ITEM, event.getX(), event.getY());
        // Spriteの採番とサーバーの採番が異なるため、別名で定義
        this.itemId = event.getObjectId();
    }

    public int getItemId() {
        return itemId;
    }

    @Override
    public void onCollisionEnter(Sprite other) {
        if (other instanceof Player) {
            Player player = (Player)other;
            if (player.isUser()) {
                MyClient.send(new Event(EventType.ITEM_DELETE).with(itemId));
            }
        }
    }
}
