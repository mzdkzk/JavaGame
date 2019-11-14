package client.actors.base;

import client.management.GameController;

public abstract class Updatable {
    public boolean started = false;
    public abstract void start();
    public abstract void update(GameController controller);
}
