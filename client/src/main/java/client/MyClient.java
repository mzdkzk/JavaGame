package client;

import client.event.Event;
import client.event.EventType;
import client.game.Game;
import client.game.Start;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient extends JFrame {
    public static final String FONT_FAMILY = "Lucida Console";

    private static Container container;

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static LocalClientThread thread;

    private static String address;
    private static String userName;
    private static int userId;

    private MyClient() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (getContentPane().getComponents()[0] instanceof Game) {
                    MyClient.send(new Event(EventType.DISCONNECT));
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyGame");
        setSize(800, 800);

        container = getContentPane();
        container.add(new Start());
    }

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

    public static void setAddress(String address) {
        MyClient.address = address;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        MyClient.userName = userName;
    }

    static void closeSocket() throws IOException {
        socket.close();
    }

    private static void initialize() {
        socket = null;
        writer = null;
        reader = null;
        thread = null;
        address = "";
        userName = "";
        userId = 0;
    }

    public static void changeComponent(Component to) {
        container.removeAll();
        container.add(to);
        container.revalidate();
        container.repaint();
        to.requestFocusInWindow();
        to.setFocusable(true);
    }

    public static void connectServer() throws IOException {
        String host = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        socket = new Socket(host, port);

        thread = new LocalClientThread();
        thread.start();
        // LocalClientThreadの処理開始より先にIDを使用しないように
        while (true) {
            System.out.println("待機中…");
            if (userId != 0) break;
        }
    }

    public static void closeThread() {
        thread.interrupt();
        initialize();
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

    public static void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfo(String message) {
        showInfo(message, message);
    }

    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(String message) {
        showError(message, message);
    }
}
