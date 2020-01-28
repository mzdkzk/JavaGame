package server;

import server.event.ServerEvent;
import server.event.ServerEventType;

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
                MyServer.send(id, new ServerEvent(ServerEventType.ITEM, itemPoint.x, itemPoint.y).with(itemId));
            }

            while (true) { //ソケットへの入力を監視する
                ServerEvent event = new ServerEvent(reader.readLine());
                System.out.println("Received from client No." + id + ", Messages: " + event.toString());
                if (event.getType() == ServerEventType.ITEM_DELETE) {
                    ItemThread.items.remove(event.getObjectId());
                }
                MyServer.sendAll(event.toString());

                if (event.getType() == ServerEventType.DISCONNECT) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("Disconnect from client No. " + id);
            isActive = false;
        }
    }
}
