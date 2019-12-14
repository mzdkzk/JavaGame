package client.actors.base;

import java.util.ArrayList;

public class Element {
    protected Sprite parent = null;
    protected ArrayList<Sprite> children = new ArrayList<>();

    public void addChild(Sprite child) {
        if (this instanceof Sprite) {
            child.parent = (Sprite)this;
        }
        children.add(child);
    }

    public void removeChild(Sprite child) {
        children.remove(child);
    }

    public ArrayList<Sprite> cloneChildren() {
        return new ArrayList<>(children);
    }

    public ArrayList<Sprite> cloneAllChildren() {
        ArrayList<Sprite> result = new ArrayList<>();
        for (Sprite child : cloneChildren()) {
            result.add(child);
            if (!child.children.isEmpty()) {
                result.addAll(child.cloneAllChildren());
            }
        }
        return result;
    }
}
