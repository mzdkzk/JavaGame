package client.actors;

import client.actors.base.Sprite;
import client.event.Event;
import client.management.Controller;

import java.awt.image.BufferedImage;

public class OtherPlayer extends Sprite {
    // TODO: PlayerまたはSpriteへの共通化の検討
    double angle;
    boolean isActive = true;

    public OtherPlayer(int x, int y) {
        super("craft.png", x, y);
    }

    @Override
    public BufferedImage getImage() {
        return getRotatedImage(angle);
    }

    public void move(Event event) {
        x = event.getX();
        y = event.getY();
        angle = event.getAngle();
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
    }
}