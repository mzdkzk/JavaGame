package client.actors;

import client.MyGameClient;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.logging.Logger;
import client.management.Controller;
import client.management.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Sprite {
    private double dx;
    private final double MAX_DX = 10;
    private double dy;
    private final double MAX_DY = 10;
    private double angle;
    private double moveSpeed = 1.0;

    public Player(int x, int y) {
        super("craft.png", x, y);
    }

    private void fire() {
        Game.addChild(new Beam(this));
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
        if (Math.abs(dx) > 0 || Math.abs(dy) > 0) {
            MyGameClient.send(new Event(EventType.move, getX(), getY(), getAngle()));
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Controller controller = Game.controller;

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
        if (Math.abs(dx) >= MAX_DX) dx = MAX_DX * Math.signum(dx);
        if (Math.abs(dy) >= MAX_DY) dy = MAX_DY * Math.signum(dy);
        dx = (Math.abs(dx) - moveSpeed / 2) * Math.signum(dx);
        dy = (Math.abs(dy) - moveSpeed / 2) * Math.signum(dy);
        move();
        Logger.update("player.worldPos", "wx=" + getX() + ",wy=" + getY());
        Logger.update("player.cameraPos", "cx=" + getRelativePos().x + ",cy=" + getRelativePos().y);
        Logger.update("player.velocity", "dx=" + dx + ",dy=" + dy);

        // TODO: 左クリックへの置き換え
        if (controller.isDown(KeyEvent.VK_SPACE)) {
            fire();
        }

        Point mousePoint = controller.getMousePoint();
        Point playerPoint = Game.player.getRelativePos();
        if (mousePoint != null) {
            angle = Math.atan2(mousePoint.y - playerPoint.y, mousePoint.x - playerPoint.x);
            Logger.update("player.degree", getDegree() + "℃");
        }
    }
}
