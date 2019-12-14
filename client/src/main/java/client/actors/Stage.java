package client.actors;

import client.actors.base.Sprite;
import client.game.resource.Resources;

public class Stage extends Sprite {
    public Stage() {
        super(Resources.GRID);
        x -= getWidth() / 2;
        y -= getHeight() / 2;
    }
}
