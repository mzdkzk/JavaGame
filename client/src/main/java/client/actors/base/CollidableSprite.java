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
        return other.getWidth() + getWidth() > Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }

    abstract public void onCollisionEnter(Sprite other);
}
