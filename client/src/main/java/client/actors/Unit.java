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

    private int getOlderUnitSize() {
        ArrayList<Sprite> cloneChildren = player.cloneChildren();
        cloneChildren.removeIf(child -> child.getId() >= id);
        return cloneChildren.size();
    }

    @Override
    public void update() {
        int unitOffset = 40;

        int unitSize = player.cloneChildren().size();
        int olderUnitSize = getOlderUnitSize();
        if (olderUnitSize >= 6) { // 2週目(このユニットより先に存在するユニットが6以上)なら
            unitOffset += 30;
            olderUnitSize -= 6;
            unitSize -= 6;
        } else if (unitSize > 6) { // 1週目かつ2週目が存在(ユニットの総数が7以上)するなら
            unitSize = 6;
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
