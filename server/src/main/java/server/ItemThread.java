package server;

import java.util.Random;

public class ItemThread extends Thread {

    public void run() {
        while (true) {
            int x = new Random().nextInt(800) - 400;
            int y = new Random().nextInt(800) - 400;
            MyServer.sendAll(new ServerEvent("ITEM", x, y));
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
