package client.actors;

import client.actors.base.Sprite;

public class Stage extends Sprite {
    public Stage() {
        super("grid.png");
        x -= getWidth() / 2;
        y -= getHeight() / 2;
    }

    @Override
    public void update() {

    }
}
