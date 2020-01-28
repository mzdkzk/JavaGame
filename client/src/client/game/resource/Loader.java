package client.game.resource;

import client.MyClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class Loader {
    private HashMap<String, BufferedImage> data = new HashMap<>();

    public Loader() {
        for (String path : Resources.ALL) {
            URL url = getClass().getClassLoader().getResource("resources/" + path);
            try {
                BufferedImage image = ImageIO.read(url);
                data.put(path, image);
            } catch (IOException e) {
                MyClient.showError(path + "の読み込みに失敗しました", e.getMessage());
            }
        }
    }

    public BufferedImage get(String path) {
        if (!data.containsKey(path)) {
            MyClient.showError(path + "が読み込まれていません");
        }
        return data.get(path);
    }
}
