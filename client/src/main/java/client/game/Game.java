package client.game;

import client.MyClient;
import client.actors.*;
import client.actors.base.Element;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.input.Controller;
import client.game.logging.Log;
import client.game.logging.Logger;
import client.game.resource.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends JPanel implements ActionListener {
    private static Element root = new Element();
    private static ArrayList<Event> eventQueue = new ArrayList<>();

    public static Loader loader = new Loader();
    public static Controller controller = new Controller();
    public static GameCamera camera = new GameCamera();
    public static Stage stage = new Stage();

    public static HashMap<Integer, Player> joinedPlayers = new HashMap<>();

    private Timer timer;
    private final int FPS = 30;

    public Game() {
        addKeyListener(controller);
        addMouseListener(controller);
        addMouseMotionListener(controller);
        setFocusable(true);

        root.addChild(stage);

        MyClient.send(new Event(EventType.UPDATE, 10, 10, 0, 3));

        root.addChild(camera);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public static Player getPlayer() {
        return joinedPlayers.get(MyClient.getUserId());
    }

    public static Element getRoot() {
        return root;
    }

    public static void addEvent(Event event) {
        eventQueue.add(event);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 前フレームで作成したイベントを受け取り、全て適用して再描画
        ArrayList<Event> clonedEventQueue = new ArrayList<>(eventQueue);
        for (Event event : clonedEventQueue) {
            boolean isJoined = joinedPlayers.containsKey(event.getSenderId());
            if (event.getType() == EventType.UPDATE && !isJoined) {
                Player player = new Player(event);
                joinedPlayers.put(event.getSenderId(), player);
                root.addChild(player);
            }
            Player sender = event.getSender();

            switch (event.getType()) {
                case UPDATE:
                    sender.setEvent(event);
                    int unitSizeDiff = event.getUnitSize() - sender.cloneChildren().size();
                    if (unitSizeDiff > 0) {
                        for (int i = 0; i < unitSizeDiff; i++) {
                            sender.addChild(new Unit(sender));
                        }
                    }
                    break;
                case FIRE:
                    root.addChild(new Beam(sender));
                    for (Sprite unit : sender.cloneChildren()) {
                        root.addChild(new Beam(unit));
                    }
                    break;
                case ITEM:
                    root.addChild(new Item(event));
                    break;
                case ITEM_DELETE:
                    for (Sprite child : root.cloneChildren()) {
                        if (child instanceof Item) {
                            Item item = (Item)child;
                            if (item.getItemId() == event.getObjectId()) {
                                sender.addChild(new Unit(sender));
                                item.destroy();
                            }
                        }
                    }
                    break;
                case DISCONNECT:
                    joinedPlayers.remove(event.getSenderId());
                    root.removeChild(sender);
                    break;
            }
            event.isDone = true;
        }
        repaint();

        // イベント適用中にも新しいイベントが追加されているので、全削除せずフラグで削除
        Game.eventQueue.removeIf(event -> event.isDone);

        // コントローラーの入力などをもとに次フレームで適用されるイベントを作成し送信する
        ArrayList<Sprite> allChildren = root.cloneAllChildren();
        for (Sprite child : allChildren) {
            child.update();
        }

        // 更新後の位置で衝突判定を行う
        for (Sprite child : allChildren) {
            for (Sprite other : allChildren) {
                // 雑負荷対策
                if (child == other) continue;
                if (child instanceof Beam && other instanceof Beam) continue;
                if (child instanceof Beam && other instanceof Item) continue;
                if (child instanceof Item && other instanceof Beam) continue;

                // 衝突したらonCollisionEnterを呼んでリストに追加
                // すでに衝突済みならリストから除去
                boolean isAlreadyCollidedWithOther = child.collisions.contains(other);
                if (child.isCollidedWith(other)) {
                    if (!isAlreadyCollidedWithOther) {
                        child.collisions.add(other);
                        child.onCollisionEnter(other);
                    }
                } else if (isAlreadyCollidedWithOther) {
                    child.collisions.remove(other);
                }
            }
        }

        Logger.update("game.childrenSize", allChildren.size() + "");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        for (Sprite child : root.cloneAllChildren()) {
            child.draw(g, this);
        }

        g.setColor(Color.BLACK);
        int logY = 0;
        for (Log log : Logger.getLogQueue()) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }
    }
}
