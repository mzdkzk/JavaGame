package client.actors;

import client.actors.base.Sprite;
import client.game.Game;

import java.awt.*;

public class GameCamera extends Sprite {
    private int width = 800;
    private int height = 800;
    private Point pos = new Point(-50, -50);

    public GameCamera() {
        super(0, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point toRelativePos(int x, int y) {
        return new Point(x - pos.x, y - pos.y);
    }

    @Override
    public void update() {
        Player player = Game.getPlayer();
        int targetX = player.getCenterX() - width / 2;
        int targetY = player.getCenterY() - height / 2;
        double easingRate = 0.2;
        pos.x += (targetX - pos.x) * easingRate;
        pos.y += (targetY - pos.y) * easingRate;
    }
}
