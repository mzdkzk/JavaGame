package client;

import java.awt.image.BufferedImage;

public class Beam extends Sprite {
    private double angle;
    private double moveSpeed = 2.0;

    Beam(Player from) {
        super("missile.png",
                (int)(from.getX() + from.width / 2),
                (int)(from.getY() + from.height / 2)
        );
        angle = from.getAngle();
    }

    @Override
    public void update(GameController controller) {
        this.x += Math.cos(angle) * moveSpeed;
        this.y += Math.sin(angle) * moveSpeed;
    }

    @Override
    public BufferedImage getImage() {
        return getRotatedImage(angle);
    }
}
