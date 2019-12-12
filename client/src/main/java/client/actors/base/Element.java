package client.actors.base;

import java.util.ArrayList;

public class Element {
    protected ArrayList<Sprite> children = new ArrayList<>();

    public void addChild(Sprite child) {
        children.add(child);
    }

    public void removeChild(Sprite child) {
        children.remove(child);
    }

    public ArrayList<Sprite> cloneChildren() {
        return new ArrayList<>(children);
    }
}
