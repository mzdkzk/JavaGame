package client.logging;

public class Log {
    String label;
    String message;

    Log(String label, String message) {
        this.label = label;
        this.message = message;
    }

    public String text() {
        return label + ": " + message;
    }
}
