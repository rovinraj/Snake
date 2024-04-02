package game;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

/**
 * Used to uniformly configure a logger that can be used by all classes.
 */
public class LoggerSetup {
    /** The path to the directory where the log file will be stored. */
    private final static String LOG_DIR = "src/resources/data/";

    /** Minimum level that the config will log. */
    private final static Level MIN_CONSOLE = Level.CONFIG;

    /** Minimum level that the file will log. */
    private final static Level MIN_FILE = Level.FINER;

    /** Holds all file handlers with the package name as the key. */
    private final static HashMap<String, FileHandler> fileHandlers = new HashMap<>();

    /**
     * This class is not instantiable as it is only used to set up a logger uniformly throughout the program.
     * @hidden
     */
    private LoggerSetup() {}

    /**
     * Returns a {@code FileHandler} for the given {@code packageName}. This allows for every package to have their own
     * file handler allowing for each package to log to one file.
     * @param packageName name of the package
     * @return the {@code FileHandler} for the given package.
     */
    private static FileHandler getFileHandler(String packageName) {
        // Check if the FileHandler for the package already exists
        if (!fileHandlers.containsKey(packageName)) {
            // Attempt to Configure and add a handler to log all minFile and above severity logs to a file with a
            // simple text format
            try {
                // Construct the log file name based on the package name
                String logFileName = LOG_DIR + packageName + "_logfile.log";
                // this filehandler will use the file at 'logFileName' and will append to the file until it reaches size of
                // Long.MAX_VALUE. it will then clear the current file and use it again as file count is set to 1. It will
                // append because of the true.
                FileHandler fileHandler = new FileHandler(logFileName, Long.MAX_VALUE, 1, true);
                fileHandler.setFormatter(new SimpleFormatter());
                fileHandler.setLevel(MIN_FILE);
                fileHandlers.put(packageName, fileHandler);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize file handler for package: " + packageName, e);
            }
        }
        return fileHandlers.get(packageName);
    }

    /**
     * Configures a {@link Logger} to log all messages at severity {@code minFile} and above to a file at
     * {@code log_path} and log all messages at {@code minConsole} and above to the console. This allows the user to
     * view detailed logging when needed without filling up the console with unnecessary information.
     * <p>
     *     The method configures a {@link ConsoleHandler} to handle console output and configures a {@link FileHandler}
     *     to log all messages to a file using a {@link SimpleFormatter} to format the logs to be easily read.
     * </p>
     * @param name name of the logger
     * @return A configured {@code Logger} of the specified name.
     */
    public static Logger getLogger(String name){
        String packageName = name.contains(".") ? name.substring(0, name.lastIndexOf('.')) : "default";
        Logger logger = Logger.getLogger(name);

        // Stops the logger from logging to console
        logger.setUseParentHandlers(false);

        // Configure and add a handler to log all minConsole and above severity logs to console
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(MIN_CONSOLE);
        logger.addHandler(consoleHandler);

        // File handler configuration
        FileHandler fileHandler = getFileHandler(packageName);
        logger.addHandler(fileHandler);

        // Allow the logger to capture everything; the logs will be sorted by the handlers
        logger.setLevel(Level.ALL);
        return logger;
    }
}
