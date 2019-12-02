package client.actors;

import client.MyClient;
import client.actors.base.CollidableSprite;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.Controller;
import client.game.Game;
import client.game.PointerType;
import client.game.logging.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends CollidableSprite {
    Event event;
    private double dx;
    private final double MAX_DX = 15;
    private double dy;
    private final double MAX_DY = 15;
    private double moveSpeed = 3.5;

    public Player(Event event) {
        super("player.png", event.getX(), event.getY());
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    private void fire() {
        MyClient.send(new Event(EventType.FIRE, x, y, angle));
    }

    @Override
    public void update() {
        // 最新イベントの適用
        x = event.getX();
        y = event.getY();
        angle = event.getAngle();

        // コントローラー入力受け取り
        Controller controller = Game.controller;
        if (controller.isKeyDown(KeyEvent.VK_A)) {
            dx -= moveSpeed;
        }
        if (controller.isKeyDown(KeyEvent.VK_D)) {
            dx += moveSpeed;
        }
        if (controller.isKeyDown(KeyEvent.VK_W)) {
            dy -= moveSpeed;
        }
        if (controller.isKeyDown(KeyEvent.VK_S)) {
            dy += moveSpeed;
        }
        if (controller.isPointerDown(PointerType.LEFT)) {
            fire();
        }
        Point mousePoint = controller.getMousePoint();
        Point playerRelativePoint = Game.getPlayer().getRelativePos();
        double nextAngle = 0.0;
        if (mousePoint != null) {
            nextAngle = Math.atan2(mousePoint.y - playerRelativePoint.y, mousePoint.x - playerRelativePoint.x);
        }

        // 減速処理
        dx = (Math.abs(dx) - moveSpeed / 2) * Math.signum(dx);
        dy = (Math.abs(dy) - moveSpeed / 2) * Math.signum(dy);
        if (Math.abs(dx) >= MAX_DX) dx = MAX_DX * Math.signum(dx);
        if (Math.abs(dy) >= MAX_DY) dy = MAX_DY * Math.signum(dy);
        if (Math.abs(dx) <= 1) dx = 0.0;
        if (Math.abs(dy) <= 1) dy = 0.0;
        int nextX = (int)(x + dx);
        int nextY = (int)(y + dy);

        // 次フレームの移動を送信
        double da = event.getAngle() - nextAngle;
        if (Math.abs(dx) > 0 || Math.abs(dy) > 0 || Math.abs(da) > 0.01) {
            MyClient.send(new Event(EventType.UPDATE, nextX, nextY, nextAngle));
        }

        // デバッグログ
        Logger.update("player.worldPos", "wx=" + x + ",wy=" + y);
        Logger.update("player.cameraPos", "cx=" + getRelativePos().x + ",cy=" + getRelativePos().y);
        Logger.update("player.velocity", "dx=" + dx + ",dy=" + dy);
        Logger.update("player.degree", getDegree() + "℃");
    }

    @Override
    public void onCollisionEnter(Sprite other) {

    }
}
