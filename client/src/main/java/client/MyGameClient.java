package client;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;

public class MyGameClient extends JFrame {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    public static int userId;

    private static PrintWriter getWriter() {
        if (writer == null) {
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer;
    }

    static void send(Object s) {
        MyGameClient.getWriter().println(s);
    }

    static BufferedReader getReader() {
        if (reader == null) {
            try {
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(streamReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reader;
    }

    static String readLine() throws IOException {
        return MyGameClient.getReader().readLine();
    }

    static void closeSocket() throws IOException {
        socket.close();
    }

    private MyGameClient() {
        connectServer();

        GameManager game = new GameManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyGame");
        setSize(game.camera.getWidth(), game.camera.getHeight());

        Container container = getContentPane(); //フレームのペインを取得する
        container.add(game);
    }

    private void connectServer() {
        try {
            socket = new Socket("localhost", 10000);
        } catch (UnknownHostException e) {
            System.err.println("ホストの IP アドレスが判定できません: " + e);
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e);
        }
        new LocalClientThread().start();
    }

    public static void main(String[] args) {
        MyGameClient game = new MyGameClient();
        game.setVisible(true);
    }
}

class LocalClientThread extends Thread {
    public void run() {
        try {
            MyGameClient.userId = Integer.parseInt(MyGameClient.readLine());
            while (true) {
                String inputLine = MyGameClient.readLine();
                if (inputLine != null) {
                    System.out.println(inputLine);
                } else {
                    break;
                }
            }
            MyGameClient.closeSocket();
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e);
        }
    }
}
