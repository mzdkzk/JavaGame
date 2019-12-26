package client.game;

import client.MyClient;
import client.game.resource.Loader;
import client.game.resource.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JPanel {
    private final String FONT_FAMILY = "Lucida Console";

    public Start() {
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addSpace(30);

        JLabel titleLabel = new JLabel("Atan.io");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 96));
        add(titleLabel);
        addSpace(10);

        add(new JLabel("サーバーアドレス"));
        addInput("localhost:10000");
        addSpace(10);

        add(new JLabel("ユーザー名"));
        addInput("username");
        addSpace(30);

        JButton startButton = new JButton("Start!");
        startButton.setFont(new Font(FONT_FAMILY, Font.PLAIN, 30));
        startButton.addActionListener(e -> {
            requestFocusInWindow();
            MyClient.changeComponent(new Game());
        });
        add(startButton);
    }

    void addSpace(int height) {
        add(Box.createRigidArea(new Dimension(0, height)));
    }

    void addInput(String defaultString) {
        JTextField addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(600, 30));
        addressField.setFont(new Font(FONT_FAMILY, Font.PLAIN, 20));
        addressField.setText(defaultString);
        addressField.setMaximumSize(addressField.getPreferredSize());
        add(addressField);
    }
}
