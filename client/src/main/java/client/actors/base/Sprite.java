package client.actors.base;

import client.game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public abstract class Sprite extends Updatable {
    protected int x;
    protected int y;
    protected double angle;
    private int width;
    private int height;
    private boolean visible;
    private BufferedImage image;

    public Sprite(String imagePath, int x, int y) {
        this.x = x;
        this.y = y;
        try {
            loadImage(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        visible = true;
    }

    private void loadImage(String path) throws IOException {
        URL url = getClass().getClassLoader().getResource(path);
        image = ImageIO.read(url);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public BufferedImage getImage() {
        if (angle == 0.0) {
            return image;
        }
        return getRotatedImage();
    }

    protected BufferedImage getRotatedImage() {
        // https://stackoverflow.com/questions/8639567/java-rotating-images
        double anchorX = image.getWidth() / 2.0;
        double anchorY = image.getHeight() / 2.0;
        AffineTransform af = new AffineTransform();
        af.rotate(angle, anchorX, anchorY);
        AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDegree() {
        return angle * 180d / Math.PI;
    }

    public double getAngle() {
        return angle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getRelativePos() {
        return Game.camera.toRelativePos(x, y);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
