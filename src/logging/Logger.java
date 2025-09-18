package logging;

final class LoggingColorEscapes {
    final static String DEBUG = "\u001B[90m"; // Grey
    final static String INFO = "\u001B[32m"; // Green
    final static String WARN = "\u001B[33m"; // Yellow
    final static String ERROR = "\u001B[31m"; // Red
};


public class Logger {

    public static void debug(String message) {
        log(LoggingColorEscapes.DEBUG, "DEBUG", message);
    }

    public static void info(String message) {
        log(LoggingColorEscapes.INFO, "INFO", message);
    }

    public static void warn(String message) {
        log(LoggingColorEscapes.WARN, "WARN", message);
    }

    public static void error(String message) {
        log(LoggingColorEscapes.ERROR, "ERROR", message);
    }

    private static void log(String color, String header, String message) {
        System.out.printf("%s[%s] %s\n", color, header, message);
    }

    // Do not instantiate
    private Logger() {}
}
