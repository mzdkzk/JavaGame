package client.actors.base;

import client.game.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class Sprite {
    protected int x;
    protected int y;
    protected double angle;
    private int width;
    private int height;
    private BufferedImage image;

    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Sprite(String imagePath) {
        this(imagePath, 0, 0);
    }

    public Sprite(String imagePath, int x, int y) {
        this(x, y);
        image = Game.resources.get(imagePath);
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
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

    public void destroy() {
        Game.removeChild(this);
    }

    abstract public void update();
}
