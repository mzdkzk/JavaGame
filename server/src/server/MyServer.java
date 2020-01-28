package server;

import server.event.ServerEvent;

import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private static final int maxConnectionCount = 100;
    static int currentId = 0;
    private static ClientThread[] threads;

    private void ready() {
        try {
            System.out.println("The server has launched!");
            ServerSocket server = new ServerSocket(10000);
            new ItemThread().start();
            threads = new ClientThread[maxConnectionCount];
            while (true) {
                Socket acceptedSocket = server.accept();
                currentId++;
                threads[currentId] = new ClientThread(acceptedSocket);
                threads[currentId].start();
            }
        } catch (Exception e) {
            System.err.println("ソケット作成時にエラーが発生しました: " + e);
        }
    }

    public static void main(String[] args) {
        new MyServer().ready();
    }

    static void send(Integer id, String str) {
        ClientThread thread = threads[id];
        thread.writer.println(str);
        thread.writer.flush();
        System.out.println("Send messages to client No. " + thread.id + ": " + str);
    }

    static void send(Integer id, ServerEvent event) {
        send(id, event.toString());
    }

    static void sendAll(String str) {
        for (int id = 0; id < threads.length; id++) {
            if (threads[id] != null && threads[id].isActive) {
                send(id, str);
            }
        }
    }

    static void sendAll(ServerEvent event) {
        sendAll(event.toString());
    }
}
