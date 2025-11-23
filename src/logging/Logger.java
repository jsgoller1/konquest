package logging;

final class LoggingColorEscapes {
    final static String DEBUG = "\u001B[90m"; // Grey
    final static String INFO = "\u001B[32m"; // Green
    final static String WARN = "\u001B[33m"; // Yellow
    final static String ERROR = "\u001B[31m"; // Red
};


public class Logger {
    private static Level currentLevel = Level.ERROR; // Default logging level

    public static void setLevel(String level) {
        switch (level) {
            case "DEBUG":
                currentLevel = Level.DEBUG;
                break;
            case "INFO":
                currentLevel = Level.DEBUG;
                break;
            case "WARN":
                currentLevel = Level.DEBUG;
                break;
            case "ERROR":
                currentLevel = Level.DEBUG;
                break;
            case "SILENCED":
                currentLevel = Level.DEBUG;
                break;
            default:
                error(String.format("Cannot set level to %d", level));
                break;
        }
    }

    public enum Level {
        DEBUG(0), INFO(1), WARN(2), ERROR(3), SILENCED(4);

        private final int loggingLevel;

        Level(int loggingLevel) {
            this.loggingLevel = loggingLevel;
        }
    }

    public static void debug(String message) {
        if (currentLevel.loggingLevel <= Level.DEBUG.loggingLevel) {
            log(LoggingColorEscapes.DEBUG, "DEBUG", message);
        }
    }

    public static void info(String message) {
        if (currentLevel.loggingLevel <= Level.INFO.loggingLevel) {
            log(LoggingColorEscapes.INFO, "INFO", message);
        }
    }

    public static void warn(String message) {
        if (currentLevel.loggingLevel <= Level.WARN.loggingLevel) {
            log(LoggingColorEscapes.WARN, "WARN", message);
        }
    }

    public static void error(String message) {
        if (currentLevel.loggingLevel <= Level.ERROR.loggingLevel) {
            log(LoggingColorEscapes.ERROR, "ERROR", message);
        }
    }

    private static void log(String color, String header, String message) {
        System.out.printf("%s[%s] %s\n", color, header, message);
    }

    // Do not instantiate
    private Logger() {}
}
