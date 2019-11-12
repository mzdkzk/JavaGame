package client;

abstract class Updatable {
    protected boolean started = false;
    abstract void start();
    abstract void update(GameController controller);
}
