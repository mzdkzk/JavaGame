package client.game;

import client.MyClient;
import client.game.resource.Loader;
import client.game.resource.Resources;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

public class Start extends JPanel {

    public Start() {
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 0));
        addSpace(30);

        JLabel titleLabel = new JLabel("Atan.io");
        titleLabel.setFont(new Font(MyClient.FONT_FAMILY, Font.BOLD, 96));
        add(titleLabel);
        addSpace(10);

        add(new JLabel("サーバーアドレス"));
        JTextField addressField = createTextField("localhost:10000");
        addressField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        add(addressField);
        addSpace(10);

        add(new JLabel("ユーザー名"));
        JTextField userNameField = createTextField("username");
        userNameField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        add(userNameField);
        addSpace(30);

        JButton startButton = new JButton("Start!");
        startButton.setFont(new Font(MyClient.FONT_FAMILY, Font.PLAIN, 30));
        startButton.addActionListener(event -> {
            if (!addressField.getText().matches("(\\w|\\d|\\.)+:\\d+")) {
                MyClient.showInfo("サーバーアドレスの形式が不正です");
            } else if (userNameField.getText().length() > 20) {
                MyClient.showInfo("ユーザー名は20文字までにしてください");
            } else {
                requestFocusInWindow();
                MyClient.setAddress(addressField.getText());
                MyClient.setUserName(userNameField.getText());
                try {
                    MyClient.changeComponent(new Game());
                } catch (UnknownHostException e) {
                    MyClient.showError("ホストのIPアドレスが判定できません", e.getMessage());
                } catch (IOException e) {
                    MyClient.showError("サーバーに接続できませんでした", e.getMessage());
                }
            }
        });
        add(startButton);
    }

    private void addSpace(int height) {
        add(Box.createRigidArea(new Dimension(0, height)));
    }

    private JTextField createTextField(String defaultString) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(600, 30));
        field.setFont(new Font(MyClient.FONT_FAMILY, Font.PLAIN, 20));
        field.setText(defaultString);
        field.setMaximumSize(field.getPreferredSize());
        return field;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new Loader().get(Resources.TITLE), 0, 0, this);
    }
}
