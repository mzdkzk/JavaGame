package client.game;

import client.MyClient;
import client.actors.*;
import client.actors.base.CollidableSprite;
import client.actors.base.Sprite;
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

public class Game extends JPanel implements ActionListener {
    private static ArrayList<Sprite> children = new ArrayList<>();
    private static ArrayList<Event> eventQueue = new ArrayList<>();

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

        addChild(stage);

        Event playerJoinEvent = new Event(EventType.UPDATE, 10, 10);
        joinPlayer(playerJoinEvent);

        addChild(camera);

        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    public static Player getPlayer() {
        return joinedPlayers.get(MyClient.getUserId());
    }

    public static void addChild(Sprite child) {
        children.add(child);
    }

    public static void removeChild(Sprite child) {
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
        // 前フレームで作成したイベントを受け取り、全て適用して再描画
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
            event.isDone = true;
        }
        repaint();

        // イベント適用中にも新しいイベントが追加されているので、削除対象はフラグで管理
        Game.eventQueue.removeIf(event -> event.isDone);

        // コントローラーの入力などをもとに次フレームで適用されるイベントを作成し送信する
        ArrayList<Sprite> children = new ArrayList<>(Game.children);
        for (Sprite child : children) {
            child.update();
        }

        // 衝突判定のためCollidableSpriteだけのリストを作る
        ArrayList<CollidableSprite> colChildren = new ArrayList<>();
        for (Sprite child : children) {
            if (child instanceof CollidableSprite) {
                CollidableSprite colChild = (CollidableSprite)child;
                colChildren.add(colChild);
            }
        }

        // 更新後の位置で衝突判定を行う
        for (CollidableSprite child : colChildren) {
            for (CollidableSprite other : colChildren) {
                if (child == other) continue;

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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        for (Sprite sprite : children) {
            Point relativePos = camera.toRelativePos(sprite.getX(), sprite.getY());
            g.drawImage(sprite.getImage(), relativePos.x, relativePos.y, this);
        }

        g.setColor(Color.BLACK);
        int logY = 0;
        for (Log log : Logger.getLogQueue()) {
            logY += 10;
            g.drawString(log.text(), 5, logY);
        }
    }
}
