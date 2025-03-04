import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Utility class for logging errors to a file
 */
public class ErrorLogger {

    private static final String ERROR_LOG_FILE = "error_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static void init() {
        createNewErrorLogFile();
    }

    // Static block to create a new error log file when the class is loaded
    static {
        createNewErrorLogFile();
    }

    /**
     * Creates a new error log file when the program starts
     */
    private static void createNewErrorLogFile() {
        try {
            File logFile = new File(ERROR_LOG_FILE);

            // If file already exists, delete it to start fresh
            if (logFile.exists()) {
                logFile.delete();
            }

            // Create a new log file
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
                writer.println("=== ERROR LOG CREATED AT " + LocalDateTime.now().format(formatter) + " ===");
                writer.println("=== All program errors will be logged here ===");
                writer.println();
            }

            System.out.println("Created new error log file: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to create error log file: " + e.getMessage());
        }
    }

    /**
     * Logs an error message to the error log file
     * @param errorMessage The error message to log
     */
    public static void logError(String errorMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            writer.println(LocalDateTime.now().format(formatter) + ": " + errorMessage);
            System.err.println("Error logged: " + errorMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to error log: " + e.getMessage());
        }
    }

    /**
     * Logs an exception to the error log file with stack trace
     * @param errorMessage The error message to log
     * @param exception The exception to log
     */
    public static void logError(String errorMessage, Exception exception) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            writer.println(LocalDateTime.now().format(formatter) + ": " + errorMessage);
            writer.println("Exception: " + exception.getClass().getName() + ": " + exception.getMessage());
            writer.println("Stack trace:");

            // Log the stack trace
            for (StackTraceElement element : exception.getStackTrace()) {
                writer.println("    at " + element.toString());
            }
            writer.println();

            System.err.println("Error logged with stack trace: " + errorMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to error log: " + e.getMessage());
        }
    }
}