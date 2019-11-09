package client;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public abstract class Sprite implements Updatable {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected boolean visible;
    protected BufferedImage image;

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

    protected void loadImage(String path) throws IOException {
        URL url = getClass().getClassLoader().getResource(path);
        image = ImageIO.read(url);
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRotatedImage(double angle) {
        // https://stackoverflow.com/questions/8639567/java-rotating-images
        double anchorX = image.getWidth() / 2.0;
        double anchorY = image.getHeight() / 2.0;
        AffineTransform af = new AffineTransform();
        af.rotate(angle, anchorX, anchorY);
        AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public Point getRelativePos() {
        return GameManager.camera.toRelativePos(getX(), getY());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
