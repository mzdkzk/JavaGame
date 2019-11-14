package client.management;

import java.util.List;
import java.util.ArrayList;

class GameLog {
    String label;
    String message;

    GameLog(String label, String message) {
        this.label = label;
        this.message = message;
    }

    String text() {
        return label + ": " + message;
    }
}

public class GameLogger {
    static List<GameLog> logQueue = new ArrayList<>();

    public static void update(String label, String message) {
        for (GameLog log : logQueue) {
            if (log.label.equals(label)) {
                log.message = message;
                return;
            }
        }
        logQueue.add(new GameLog(label, message));
    }
}
