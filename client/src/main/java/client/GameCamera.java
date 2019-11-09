package client;

import java.awt.*;

class GameCamera implements Updatable {
    private int width = 500;
    private int height = 500;
    private Point pos = new Point(-50, -50);

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    public Point toRelativePos(int x, int y) {
        return new Point(x - pos.x, y - pos.y);
    }

    @Override
    public void update(GameController controller) {
        pos.move(GameManager.player.getX(), GameManager.player.getY());
    }
}
