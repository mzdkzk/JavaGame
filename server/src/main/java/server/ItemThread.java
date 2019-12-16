package server;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class ItemThread extends Thread {
    static int currentId;
    static HashMap<Integer, Point> items = new HashMap<>();

    public void run() {
        while (true) {
            int x = new Random().nextInt(800) - 400;
            int y = new Random().nextInt(800) - 400;

            items.put(++currentId, new Point(x, y));
            System.out.println(String.format("(%d,%d)にアイテム配置", x, y));

            MyServer.sendAll(new ServerEvent("ITEM", x, y));
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
