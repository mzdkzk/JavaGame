package client.game.input;

import client.game.logging.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller implements KeyListener, MouseMotionListener, MouseListener {
    private Point mousePoint;
    private int pointerDown;
    private ArrayList<Integer> keyQueue = new ArrayList<>();

    public boolean isKeyDown(Integer keyCode) {
        return keyQueue.contains(keyCode);
    }

    public boolean isPointerDown(int pointerType) {
        return pointerDown == pointerType;
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
        mousePoint = e.getPoint();
        Logger.update("mouse.move", "x=" + mousePoint.x + ",y=" + mousePoint.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = e.getPoint();
        Logger.update("mouse.move", "x=" + mousePoint.x + ",y=" + mousePoint.y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        pointerDown = e.getButton();
        Logger.update("mouse.click", "no." + e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pointerDown = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        pointerDown = 0;
    }
}
