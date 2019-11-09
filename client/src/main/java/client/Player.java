package client;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Sprite {
    private double dx;
    private double dy;
    private double angle;
    private double moveSpeed = 1.0;

    Player(int x, int y) {
        super("craft.png", x, y);
    }

    private void fire() {
        GameManager.addChild(new Beam(this));
    }

    double getDegree() {
        return angle * 180d / Math.PI;
    }

    double getAngle() {
        return angle;
    }

    @Override
    public BufferedImage getImage() {
        return this.getRotatedImage(angle);
    }

    private void move() {
        x += dx;
        y += dy;
    }

    @Override
    public void update(GameController controller) {
        if (controller.isDown(KeyEvent.VK_A)) {
            dx -= moveSpeed;
        }
        if (controller.isDown(KeyEvent.VK_D)) {
            dx += moveSpeed;
        }
        if (controller.isDown(KeyEvent.VK_W)) {
            dy -= moveSpeed;
        }
        if (controller.isDown(KeyEvent.VK_S)) {
            dy += moveSpeed;
        }
        dx = (Math.abs(dx) - moveSpeed / 2) * Math.signum(dx);
        dy = (Math.abs(dy) - moveSpeed / 2) * Math.signum(dy);
        move();

        // TODO: 左クリックへの置き換え
        if (controller.isDown(KeyEvent.VK_SPACE)) {
            fire();
        }

        Point mousePoint = controller.getMousePoint();
        Point playerPoint = GameManager.player.getRelativePos();
        if (mousePoint != null) {
            angle = Math.atan2(mousePoint.y - playerPoint.y, mousePoint.x - playerPoint.x);
            GameLogger.update("player.degree", getDegree() + "℃");
        }
    }
}
