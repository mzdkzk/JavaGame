package client.management;

import client.logging.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Controller implements KeyListener, MouseMotionListener {
    private Point mousePoint;
    private ArrayList<Integer> keyQueue = new ArrayList<>();

    public boolean isDown(Integer keyCode) {
        return keyQueue.contains(keyCode);
    }

    public Point getMousePoint() {
        return mousePoint;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!keyQueue.contains(keyCode)) {
            keyQueue.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyQueue.removeIf(key -> key == e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
        Logger.update("mouse", "x=" + mousePoint.x + ",y=" + mousePoint.y);
    }
}
