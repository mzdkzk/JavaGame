package client.actors;

import client.actors.base.Sprite;
import client.game.resource.Resources;

import java.util.ArrayList;

public class Unit extends Sprite {
    Player player;

    public Unit(Player player) {
        super(Resources.UNIT);
        this.x = player.getCenterX();
        this.y = player.getCenterY();
        this.angle = player.getAngle();
        this.player = player;
    }

    private int olderUnitSize() {
        ArrayList<Sprite> cloneChildren = player.cloneChildren();
        cloneChildren.removeIf(child -> child.getId() >= id);
        return cloneChildren.size();
    }

    @Override
    public void update() {
        int unitSize = player.cloneChildren().size();
        double divideAngle = (2 * Math.PI) / unitSize;

        x = (int)(Math.cos(divideAngle * olderUnitSize()) * 70) + player.getCenterX() - getWidth() / 2;
        y = (int)(Math.sin(divideAngle * olderUnitSize()) * 70) + player.getCenterY() - getHeight() / 2;
        angle = player.getAngle();
    }
}
