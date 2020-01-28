package client.game;

import client.MyClient;
import client.actors.*;
import client.actors.base.Element;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.input.Controller;
import client.game.logging.Logger;
import client.game.resource.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    public static Loader loader;
    public static Controller controller;
    public static GameCamera camera;
    public static Stage stage;
    public static HashMap<Integer, Player> joinedPlayers;
    private static Element root;
    private static ArrayList<Event> eventQueue;

    private Timer timer;
    private final int FPS = 30;

    public Game() throws IOException {
        initialize();
        MyClient.connectServer();

        addKeyListener(controller);
        addMouseListener(controller);
        addMouseMotionListener(controller);

        root.addChild(stage);

        Random r = new Random();
        int x = r.nextInt(800) - 400;
        int y = r.nextInt(800) - 400;
        MyClient.send(new Event(EventType.UPDATE, x, y, 0, 3));

        root.addChild(camera);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public void initialize() {
        root = new Element();
        eventQueue = new ArrayList<>();
        loader = new Loader();
        controller = new Controller();
        camera = new GameCamera();
        stage = new Stage();
        joinedPlayers = new HashMap<>();
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
                // 初めてUPDATEイベントを送信したユーザーをジョイン
                Player player = new Player(event);
                joinedPlayers.put(event.getSenderId(), player);
                root.addChild(player);

                // ジョインしたユーザーにユニット付与
                int unitSizeDiff = event.getUnitSize() - player.cloneChildren().size();
                if (unitSizeDiff > 0) {
                    for (int i = 0; i < unitSizeDiff; i++) {
                        player.addChild(new Unit(player));
                    }
                }
            }
            Player sender = event.getSender();

            switch (event.getType()) {
                case UPDATE:
                    sender.setEvent(event);
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
                            if (item.getItemId() != event.getObjectId()) {
                                continue;
                            }
                            if (sender.canAddUnit()) {
                                sender.addChild(new Unit(sender));
                                item.destroy();
                            }
                        }
                    }
                    break;
                case DISCONNECT:
                    if (event.isOther()) {
                        joinedPlayers.remove(event.getSenderId());
                        root.removeChild(sender);
                    } else {
                        MyClient.closeThread();
                        MyClient.changeComponent(new Start());
                        timer.stop();
                        return;
                    }
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

        /*g.setColor(Color.BLACK);
        int logY = 0;
        for (Log log : Logger.getLogQueue()) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }*/
    }
}
