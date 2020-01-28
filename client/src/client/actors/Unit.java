package client.actors;

import client.actors.base.Hittable;
import client.actors.base.Sprite;
import client.game.resource.Resources;

import java.util.ArrayList;

public class Unit extends Sprite implements Hittable {
    Player player;

    private final int MAX_HP = 5;
    private int hp = MAX_HP;

    public Unit(Player player) {
        super(Resources.UNIT);
        this.x = player.getCenterX();
        this.y = player.getCenterY();
        this.angle = player.getAngle();
        this.player = player;
    }

    private int getOlderUnitSize() {
        ArrayList<Sprite> cloneChildren = player.cloneChildren();
        cloneChildren.removeIf(child -> child.getId() >= id);
        return cloneChildren.size();
    }

    @Override
    public void hit(int damage) {
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    @Override
    public void update() {
        int unitOffset = 40;
        int unitFirstLineSize = 12;

        int unitSize = player.cloneChildren().size();
        int olderUnitSize = getOlderUnitSize();
        if (olderUnitSize >= unitFirstLineSize) { // 2週目(このユニットより先に存在するユニットが6以上)なら
            unitOffset += unitOffset / 2;
            olderUnitSize -= unitFirstLineSize;
            unitSize -= unitFirstLineSize;
        } else if (unitSize > unitFirstLineSize) { // 1週目かつ2週目が存在(ユニットの総数が7以上)するなら
            unitSize = unitFirstLineSize;
        }
        double divideAngle = (2 * Math.PI) / unitSize;

        int targetX = (int)(Math.cos(divideAngle * olderUnitSize) * unitOffset) + player.getCenterX() - getWidth() / 2;
        int targetY = (int)(Math.sin(divideAngle * olderUnitSize) * unitOffset) + player.getCenterY() - getHeight() / 2;

        double easingRate = 0.4;
        x += (targetX - x) * easingRate;
        y += (targetY - y) * easingRate;
        angle = player.getAngle();
    }
}
