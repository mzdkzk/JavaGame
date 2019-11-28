package client;

import client.event.Event;
import client.event.EventType;
import client.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient extends JFrame {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static int userId;

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

    public static void send(Event event) {
        MyClient.getWriter().println(event.toString());
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
        return MyClient.getReader().readLine();
    }


    public static int getUserId() {
        return userId;
    }

    static void closeSocket() throws IOException {
        socket.close();
    }

    private MyClient() {
        connectServer();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MyClient.send(new Event(EventType.DISCONNECT));
            }
        });

        Game game = new Game();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyGame");
        setSize(Game.camera.getWidth(), Game.camera.getHeight());

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
        MyClient game = new MyClient();
        game.setVisible(true);
    }

    private static class LocalClientThread extends Thread {
        public void run() {
            try {
                userId = Integer.parseInt(readLine());
                while (true) {
                    String inputLine = readLine();
                    if (inputLine != null) {
                        Event event = new Event(inputLine);
                        // 自身が送信したイベントも受け取る
                        Game.addEvent(event);
                        System.out.println(event.toString());
                    } else {
                        break;
                    }
                }
                closeSocket();
            } catch (IOException e) {
                System.err.println("エラーが発生しました: " + e);
            }
        }
    }
}