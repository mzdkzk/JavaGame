package client.game;

import client.MyClient;
import client.game.resource.Loader;
import client.game.resource.Resources;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JPanel {
    private final String FONT_FAMILY = "Lucida Console";

    public Start() {
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 0));
        addSpace(30);

        JLabel titleLabel = new JLabel("Atan.io");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 96));
        add(titleLabel);
        addSpace(10);

        add(new JLabel("サーバーアドレス"));
        JTextField addressField = createTextField("localhost:10000");
        add(addressField);
        addSpace(10);

        add(new JLabel("ユーザー名"));
        JTextField userNameField = createTextField("username");
        add(userNameField);
        addSpace(30);

        JButton startButton = new JButton("Start!");
        startButton.setFont(new Font(FONT_FAMILY, Font.PLAIN, 30));
        startButton.addActionListener(e -> {
            if (addressField.getText().matches("(\\w|\\d|\\.)+:\\d+")) {
                requestFocusInWindow();
                MyClient.setAddress(addressField.getText());
                MyClient.setUserName(userNameField.getText());
                MyClient.changeComponent(new Game());
            } else {
                MyClient.showInfo("サーバーアドレスの形式が不正です");
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
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 20));
        field.setText(defaultString);
        field.setMaximumSize(field.getPreferredSize());
        return field;
    }
}
