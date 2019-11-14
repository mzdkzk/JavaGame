package client.actors.base;

public abstract class Updatable {
    public boolean started = false;
    public abstract void start();
    public abstract void update();
}
