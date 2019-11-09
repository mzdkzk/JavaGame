package client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class GameController implements KeyListener, MouseMotionListener {
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
        GameLogger.update("mouse", "x=" + mousePoint.x + ",y=" + mousePoint.y);
    }
}
