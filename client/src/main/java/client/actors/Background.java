package client.actors;

import client.actors.base.Sprite;

public class Background extends Sprite {

    public Background() {
        super("grid.png");
        x -= getWidth() / 2;
        y -= getHeight() / 2;
    }

    @Override
    public void update() {

    }

    @Override
    public void onCollisionEnter(Sprite other) {

    }
}
