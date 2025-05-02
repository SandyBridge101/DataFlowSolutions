package utils;

import java.io.InputStream;
import java.util.logging.*;

public class LoggerUtil {

    static {
        try (InputStream configStream = LoggerUtil.class.getResourceAsStream("/logging.properties")) {
            if (configStream != null) {
                LogManager.getLogManager().readConfiguration(configStream);
            } else {
                System.err.println("logging.properties file not found in classpath.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
        }
    }

    // Recommended version: returns logger for the calling class
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    // Original version retained for compatibility
    public static Logger getLogger() {
        return Logger.getLogger(LoggerUtil.class.getName());
    }
}
