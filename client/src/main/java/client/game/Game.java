package client.game;

import client.MyClient;
import client.actors.*;
import client.actors.base.CollidableSprite;
import client.actors.base.Element;
import client.actors.base.Sprite;
import client.event.Event;
import client.event.EventType;
import client.game.input.Controller;
import client.game.logging.Log;
import client.game.logging.Logger;
import client.game.resource.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends JPanel implements ActionListener {
    private static Element root = new Element();
    private static ArrayList<Event> eventQueue = new ArrayList<>();

    public static ResourceManager resources = new ResourceManager();
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

        Event playerJoinEvent = new Event(EventType.UPDATE, 10, 10);
        joinPlayer(playerJoinEvent);

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

    private static void joinPlayer(Event event) {
        Player player = new Player(event);
        if (event.isOther()) {
            player = new OtherPlayer(event);
        }
        joinedPlayers.put(event.getSenderId(), player);
        root.addChild(player);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 前フレームで作成したイベントを受け取り、全て適用して再描画
        ArrayList<Event> clonedEventQueue = new ArrayList<>(eventQueue);
        for (Event event : clonedEventQueue) {
            if (!joinedPlayers.containsKey(event.getSenderId())) {
                joinPlayer(event);
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
        for (Sprite child : root.cloneChildren()) {
            child.updateAll();
        }

        // 衝突判定のためCollidableSpriteだけのリストを作る
        ArrayList<CollidableSprite> colChildren = new ArrayList<>();
        for (Sprite child : root.cloneChildren()) {
            if (child instanceof CollidableSprite) {
                CollidableSprite colChild = (CollidableSprite)child;
                colChildren.add(colChild);
            }
        }

        // 更新後の位置で衝突判定を行う
        for (CollidableSprite child : colChildren) {
            for (CollidableSprite other : colChildren) {
                if (child == other) continue;
                if (child instanceof Beam && other instanceof Beam) continue;

                boolean isOtherInCollision = child.collisions.contains(other);
                if (child.isInCollision(other)) {
                    if (!isOtherInCollision) {
                        child.collisions.add(other);
                        child.onCollisionEnter(other);
                    }
                } else if (isOtherInCollision) {
                    child.collisions.remove(other);
                }
            }
        }

        Logger.update("game.childrenSize", root.cloneChildren().size() + "");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        for (Sprite child : root.cloneChildren()) {
            child.drawAll(g, this);
        }

        g.setColor(Color.BLACK);
        int logY = 0;
        for (Log log : Logger.getLogQueue()) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }
    }
}
