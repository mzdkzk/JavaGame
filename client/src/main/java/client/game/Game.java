package client.game;

import client.actors.GameCamera;
import client.actors.OtherPlayer;
import client.actors.Player;
import client.actors.Star;
import client.actors.base.Sprite;
import client.actors.base.Updatable;
import client.event.Event;
import client.game.logging.Log;
import client.game.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    private static ArrayList<Updatable> children = new ArrayList<>();
    private static ArrayList<Event> eventQueue = new ArrayList<>();

    public static Controller controller = new Controller();
    public static GameCamera camera = new GameCamera();

    public static Player player = new Player(10, 10);
    public static HashMap<Integer, OtherPlayer> otherPlayers = new HashMap<>();

    private Timer timer;
    private final int FPS = 30;

    public Game() {
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
        ArrayList<Event> eventQueue = new ArrayList<>(Game.eventQueue);
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
        Game.eventQueue.clear();

        ArrayList<Updatable> children = new ArrayList<>(Game.children);
        for (Updatable child : children) {
            if (!child.started) {
                child.started = true;
                child.start();
            }
            child.update();
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
        for (Log log : Logger.getLogQueue()) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }
    }
}
