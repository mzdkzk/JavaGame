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
        int x = player.getX() - width / 2 + player.getWidth() / 2;
        int y = player.getY() - height / 2 + player.getHeight() / 2;
        pos.move(x, y);
    }

    @Override
    public void onCollisionEnter(Sprite other) {

    }
}
