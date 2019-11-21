package client.actors;

import client.MyClient;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.Controller;
import client.game.Game;
import client.game.logging.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Sprite {
    Event event;
    private double dx;
    private final double MAX_DX = 10;
    private double dy;
    private final double MAX_DY = 10;
    private double moveSpeed = 1.0;

    public Player(Event event) {
        super("craft.png", event.getX(), event.getY());
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    private void fire() {
        MyClient.send(new Event(EventType.FIRE, x, y, angle));
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        // 最新イベントの適用
        x = event.getX();
        y = event.getY();
        angle = event.getAngle();

        // コントローラー入力受け取り
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
        // TODO: 左クリックへの置き換え
        if (controller.isDown(KeyEvent.VK_SPACE)) {
            fire();
        }
        Point mousePoint = controller.getMousePoint();
        Point playerRelativePoint = Game.getPlayer().getRelativePos();
        double nextAngle = 0.0;
        if (mousePoint != null) {
            nextAngle = Math.atan2(mousePoint.y - playerRelativePoint.y, mousePoint.x - playerRelativePoint.x);
        }

        // 減速処理
        if (Math.abs(dx) >= MAX_DX) dx = MAX_DX * Math.signum(dx);
        if (Math.abs(dy) >= MAX_DY) dy = MAX_DY * Math.signum(dy);
        dx = (Math.abs(dx) - moveSpeed / 2) * Math.signum(dx);
        dy = (Math.abs(dy) - moveSpeed / 2) * Math.signum(dy);
        if (dx * dx == 0.0) dx = 0.0;
        if (dy * dy == 0.0) dy = 0.0;
        int nextX = (int)(x + dx);
        int nextY = (int)(y + dy);

        // 次フレームの移動を送信
        MyClient.send(new Event(EventType.UPDATE, nextX, nextY, nextAngle));

        // デバッグログ
        Logger.update("player.worldPos", "wx=" + x + ",wy=" + y);
        Logger.update("player.cameraPos", "cx=" + getRelativePos().x + ",cy=" + getRelativePos().y);
        Logger.update("player.velocity", "dx=" + dx + ",dy=" + dy);
        Logger.update("player.degree", getDegree() + "℃");
    }
}
