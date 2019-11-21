package client.game;

import client.MyClient;
import client.actors.*;
import client.actors.base.Sprite;
import client.actors.base.Updatable;
import client.event.Event;
import client.event.EventType;
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

    public static HashMap<Integer, Player> joinedPlayers = new HashMap<>();

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

        Event playerJoinEvent = new Event(EventType.UPDATE, 10, 10);
        joinPlayer(playerJoinEvent);

        addChild(camera);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public static Player getPlayer() {
        return joinedPlayers.get(MyClient.getUserId());
    }

    public static void addChild(Updatable child) {
        children.add(child);
    }

    public static void removeChild(Updatable child) {
        children.remove(child);
    }

    public static void addEvent(Event event) {
        eventQueue.add(event);
    }

    private static void joinPlayer(Event event) {
        Player player = new Player(event);
        if (event.isOther()) {
            player = new OtherPlayer(event);
        }
        joinedPlayers.put(event.getSenderId(), player);
        addChild(player);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 前フレームで作成したイベントを受け取り、全て適用する
        ArrayList<Event> eventQueue = new ArrayList<>(Game.eventQueue);
        for (Event event : eventQueue) {
            if (!joinedPlayers.containsKey(event.getSenderId())) {
                joinPlayer(event);
            }
            Player sender = event.getSender();
            switch (event.getType()) {
                case UPDATE:
                    sender.setEvent(event);
                    break;
                case FIRE:
                    addChild(new Beam(sender));
                    break;
                case DISCONNECT:
                    joinedPlayers.remove(event.getSenderId());
                    removeChild(sender);
                    break;
            }
        }

        // 適用したイベントを元に全要素を再描画
        repaint();
        Game.eventQueue.clear();

        // コントローラーの入力などをもとに次フレームで適用されるイベントを作成し送信する
        ArrayList<Updatable> children = new ArrayList<>(Game.children);
        for (Updatable child : children) {
            if (!child.started) {
                child.started = true;
                child.start();
            }
            child.update();
        }
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
