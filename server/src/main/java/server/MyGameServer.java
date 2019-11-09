package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyGameServer {
    private static final int maxConnectionCount = 100;
    static int currentId = 0;
    private static ClientThread[] threads;

    private void ready() {
        try {
            System.out.println("The server has launched!");
            ServerSocket server = new ServerSocket(10000);
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
        new MyGameServer().ready();
    }

    static void SendAll(String str) {
        for (ClientThread thread : threads) {
            if (thread != null && thread.isActive) {
                thread.writer.println(str);
                thread.writer.flush();
                System.out.println("Send messages to client No. " + thread.id);
            }
        }
    }
}

class ClientThread extends Thread {
    boolean isActive = true;
    BufferedReader reader;
    PrintWriter writer;
    int id;

    ClientThread(Socket socket) throws IOException {
        super();

        this.id = MyGameServer.currentId;

        InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(streamReader);
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        try {
            writer.println("[Server]id: " + id + "のスレッドが開始されました");

            while (true) { //ソケットへの入力を監視する
                String str = reader.readLine();
                System.out.println("Received from client No." + id + ", Messages: " + str);
                if (str != null) {//このソケット（バッファ）に入力があるかをチェック
                    MyGameServer.SendAll(str);
                }
            }
        } catch (Exception e) {
            System.out.println("Disconnect from client No. " + id);
            isActive = false;
        }
    }
}
