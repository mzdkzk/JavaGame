package server;

import server.event.ServerEvent;
import server.event.ServerEventType;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class ItemThread extends Thread {
    static int currentId;
    static HashMap<Integer, Point> items = new HashMap<>();
    static final int MAX_ITEM_COUNT = 20;

    public void run() {
        while (true) {
            int x = new Random().nextInt(800) - 400;
            int y = new Random().nextInt(800) - 400;

            if (items.size() <= MAX_ITEM_COUNT) {
                currentId++;
                items.put(currentId, new Point(x, y));
                System.out.println(String.format("(%d, %d)にアイテム配置", x, y));
                MyServer.sendAll(new ServerEvent(ServerEventType.ITEM, x, y).with(currentId));
            }

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
