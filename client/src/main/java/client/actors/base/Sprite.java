package client.actors.base;

import client.game.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public abstract class Sprite extends Element {
    protected int id;
    private static int currentId;
    protected int x;
    protected int y;
    protected double angle;
    private int width;
    private int height;
    private BufferedImage image;
    public ArrayList<Sprite> collisions = new ArrayList<>();

    public Sprite(int x, int y) {
        this.id = ++currentId;
        this.x = x;
        this.y = y;
    }

    public Sprite(String imagePath) {
        this(imagePath, 0, 0);
    }

    public Sprite(String imagePath, int x, int y) {
        this(x, y);
        image = Game.loader.get(imagePath);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public int getId() {
        return id;
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
        Game.getRoot().removeChild(this);
    }

    public void drawAll(Graphics g, ImageObserver observer) {
        draw(g, observer);
        if (!children.isEmpty()) {
            for (Sprite child : children) {
                child.drawAll(g, observer);
            }
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        Point relativePos = Game.camera.toRelativePos(x, y);
        g.drawImage(getImage(), relativePos.x, relativePos.y, observer);
    }

    public void updateAll() {
        update();
        if (!children.isEmpty()) {
            for (Sprite child : children) {
                child.updateAll();
            }
        }
    }

    public void update() {

    }

    public boolean isInCollision(Sprite other) {
        // Spriteのwidthを半径とみなして、2つの半径の和が2点間距離より大きいかどうか
        // 0.5より大きい数を掛けると判定が小さくなる
        double minifyCollisionRate = 0.5;
        double sumRadius = (other.getWidth() + getWidth()) * 0.5 * minifyCollisionRate;
        double distance = Math.sqrt(
                Math.pow(other.getCenterX() - getCenterX(), 2) + Math.pow(other.getCenterY() - getCenterY(), 2)
        );
        return sumRadius > distance;
    }

    public void onCollisionEnter(Sprite other) {

    }
}
