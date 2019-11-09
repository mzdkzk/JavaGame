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

    static PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(socket.getOutputStream(), true);
        }
        return writer;
    }

    static BufferedReader getReader() throws IOException {
        if (reader == null) {
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader((streamReader));
        }
        return reader;
    }

    static void closeSocket() throws IOException {
        socket.close();
    }

    private MyGameClient() {
        GameManager game = new GameManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("MyGame");
        setSize(game.camera.getWidth(), game.camera.getHeight());

        Container container = getContentPane(); //フレームのペインを取得する
        container.add(game);

        connectServer();
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
            BufferedReader bufferedReader = MyGameClient.getReader();
            PrintWriter printWriter = MyGameClient.getWriter();
            while (true) {
                String inputLine = bufferedReader.readLine();
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
