package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameManager extends JPanel implements ActionListener {
    // TODO: staticにする
    private GameController controller = new GameController();
    private static ArrayList<Updatable> children = new ArrayList<>();
    private static ArrayList<Event> eventQueue = new ArrayList<>();
    public static GameCamera camera = new GameCamera();

    public static Player player = new Player(10, 10);
    public static HashMap<Integer, OtherPlayer> otherPlayers = new HashMap<>();

    private Timer timer;
    private final int FPS = 30;

    GameManager() {
        addKeyListener(controller);
        addMouseMotionListener(controller);
        setFocusable(true);

        setBackground(Color.BLACK);

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int x = r.nextInt(1000) - 500;
            int y = r.nextInt(1000) - 500;
            addChild(new Star(x, y));
        }

        addChild(player);
        addChild(camera);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public static void addChild(Updatable child) {
        children.add(child);
    }

    public static void addEvent(Event event) {
        eventQueue.add(event);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Event> eventQueue = new ArrayList<>(GameManager.eventQueue);
        for (Event event : eventQueue) {
            switch (event.getType()) {
                case join:
                    break;
                case move:
                    if (otherPlayers.containsKey(event.getSenderId())) {
                        OtherPlayer joinedOther = otherPlayers.get(event.getSenderId());
                        joinedOther.move(event);
                    } else {
                        OtherPlayer otherPlayer = new OtherPlayer(event.getX(), event.getY());
                        otherPlayers.put(event.getSenderId(), otherPlayer);
                        addChild(otherPlayer);
                    }
                    break;
            }
        }
        GameManager.eventQueue.clear();

        ArrayList<Updatable> children = new ArrayList<>(GameManager.children);
        for (Updatable child : children) {
            if (!child.started) {
                child.started = true;
                child.start();
            }
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
