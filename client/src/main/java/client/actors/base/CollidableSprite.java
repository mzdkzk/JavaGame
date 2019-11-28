package client.actors.base;

import java.util.ArrayList;

public abstract class CollidableSprite extends Sprite {
    public ArrayList<CollidableSprite> collisions = new ArrayList<>();

    public CollidableSprite(int x, int y) {
        super(x, y);
    }

    public CollidableSprite(String imagePath) {
        super(imagePath);
    }

    public CollidableSprite(String imagePath, int x, int y) {
        super(imagePath, x, y);
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

    abstract public void onCollisionEnter(Sprite other);
}
