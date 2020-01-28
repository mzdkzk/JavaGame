package client.game.logging;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static List<Log> logQueue = new ArrayList<>();

    public static List<Log> getLogQueue() {
        return logQueue;
    }

    public static void update(String label, String message) {
        for (Log log : logQueue) {
            if (log.label.equals(label)) {
                log.message = message;
                return;
            }
        }
        logQueue.add(new Log(label, message));
    }
}
