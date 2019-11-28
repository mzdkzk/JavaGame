package client.actors.base;

import client.game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public abstract class Sprite {
    protected int x;
    protected int y;
    protected double angle;
    private int width;
    private int height;
    private BufferedImage image;

    public ArrayList<Sprite> spritesInCollision = new ArrayList<>();
    private final int COLLISION_CIRCLE_DIAMETER = 10;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Sprite(String imagePath) {
        this(imagePath, 0, 0);
    }

    public Sprite(String imagePath, int x, int y) {
        this(x, y);
        try {
            loadImage(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private BufferedImage getRotatedImage() {
        // https://stackoverflow.com/questions/8639567/java-rotating-images
        double anchorX = image.getWidth() / 2.0;
        double anchorY = image.getHeight() / 2.0;
        AffineTransform af = new AffineTransform();
        af.rotate(angle, anchorX, anchorY);
        AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public boolean isInCollision(Sprite other) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int otherCenterX = other.x + other.width / 2;
        int otherCenterY = other.y + other.height / 2;
        return Math.pow(centerX - otherCenterX, 2) + Math.pow(centerY - otherCenterY, 2) < Math.pow(COLLISION_CIRCLE_DIAMETER, 2);
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

    abstract public void update();

    abstract public void onCollisionEnter(Sprite other);
}
