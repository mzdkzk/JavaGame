package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameManager extends JPanel implements ActionListener {
    private GameController controller = new GameController();
    private static ArrayList<Updatable> children = new ArrayList<>();
    public static GameCamera camera = new GameCamera();
    public static Player player = new Player(10, 10);
    private Timer timer;
    private final int FPS = 30;

    GameManager() {
        addKeyListener(controller);
        addMouseMotionListener(controller);
        setFocusable(true);

        setBackground(Color.BLACK);

        addChild(camera);
        addChild(player);

        timer = new Timer(FPS, this);
        timer.start();
    }

    public static void addChild(Updatable child) {
        children.add(child);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Updatable> children = (ArrayList<Updatable>)GameManager.children.clone();
        for (Updatable child : children) {
            child.update(controller);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        for (Updatable child : children) {
            if (child instanceof Sprite) {
                Sprite sprite = (Sprite)child;
                Point relativePos = camera.toRelativePos(sprite.getX(), sprite.getY());
                g.drawImage(sprite.getImage(), relativePos.x, relativePos.y, this);
            }
        }

        g.setColor(Color.WHITE);
        int logY = 0;
        for (GameLog log : GameLogger.logQueue) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }
    }
}
