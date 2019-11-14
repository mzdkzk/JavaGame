package client.actors;

import client.actors.base.Updatable;
import client.management.GameController;
import client.management.GameManager;

import java.awt.Point;

public class GameCamera extends Updatable {
    private int width = 500;
    private int height = 500;
    private Point pos = new Point(-50, -50);

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
    public void start() {

    }

    @Override
    public void update(GameController controller) {
        Player player = GameManager.player;
        int x = player.getX() - width / 2 + player.getWidth() / 2;
        int y = player.getY() - height / 2 + player.getHeight() / 2;
        pos.move(x, y);
    }
}
