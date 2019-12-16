package server;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientThread extends Thread {
    boolean isActive = true;
    BufferedReader reader;
    PrintWriter writer;
    int id;

    ClientThread(Socket socket) throws IOException {
        super();

        this.id = MyServer.currentId;

        InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(streamReader);
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        try {
            writer.println(id);

            for (Integer itemId : ItemThread.items.keySet()) {
                Point itemPoint = ItemThread.items.get(itemId);
                MyServer.send(id, new ServerEvent("ITEM", itemPoint.x, itemPoint.y));
            }

            while (true) { //ソケットへの入力を監視する
                String str = reader.readLine();
                System.out.println("Received from client No." + id + ", Messages: " + str);
                if (str != null) {//このソケット（バッファ）に入力があるかをチェック
                    MyServer.sendAll(str);
                }
            }
        } catch (Exception e) {
            System.out.println("Disconnect from client No. " + id);
            isActive = false;
        }
    }
}
