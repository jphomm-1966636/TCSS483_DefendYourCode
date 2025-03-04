import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

/**
 * Handles validation of user input for names, integers, and file names
 */
public class InputValidator {
    // Constants for name validation
    private static final int MAX_NAME_LENGTH = 50;
    private static final String NAME_PATTERN = "^[a-zA-Z\\-'\\s]+$";

    // Constants for int range
    private static final int MIN_INT_VALUE = Integer.MIN_VALUE; // -2,147,483,648
    private static final int MAX_INT_VALUE = Integer.MAX_VALUE; // 2,147,483,647

    // Constants for file name validation
    private static final int MAX_FILENAME_LENGTH = 255; // Common filesystem limit
    private static final String FILENAME_PATTERN = "^[a-zA-Z0-9._\\-]+$"; // Alphanumeric, period, underscore, hyphen

    /**
     * Prompts for and validates a name input
     * @param scanner The Scanner object for input
     * @param nameType "first" or "last" to indicate which name is being requested
     * @return The validated name
     */
    public static String getValidName(Scanner scanner, String nameType) {
        String name = "";
        boolean isValid = false;

        System.out.println("\n----- " + nameType.toUpperCase() + " NAME INPUT -----");
        System.out.println("Please enter your " + nameType + " name following these rules:");
        System.out.println("1. Maximum " + MAX_NAME_LENGTH + " characters");
        System.out.println("2. Only letters (A-Z, a-z), hyphens (-), apostrophes ('), and spaces are allowed");
        System.out.println("3. Cannot be empty");

        while (!isValid) {
            System.out.print("\nEnter your " + nameType + " name: ");
            name = scanner.nextLine().trim();

            // Check if name is empty
            if (name.isEmpty()) {
                System.out.println("Error: " + nameType + " name cannot be empty. Please try again.");
                continue;
            }

            // Check length
            if (name.length() > MAX_NAME_LENGTH) {
                System.out.println("Error: " + nameType + " name exceeds maximum length of " +
                        MAX_NAME_LENGTH + " characters. Please try again.");
                continue;
            }

            // Check character validity using regex
            Pattern pattern = Pattern.compile(NAME_PATTERN);
            Matcher matcher = pattern.matcher(name);

            if (!matcher.matches()) {
                System.out.println("Error: " + nameType + " name contains invalid characters. " +
                        "Only letters, hyphens, apostrophes, and spaces are allowed. Please try again.");
                continue;
            }

            isValid = true;
        }

        return name;
    }

    /**
     * Prompts for and validates an integer input
     * @param scanner The Scanner object for input
     * @param ordinal "first" or "second" to indicate which integer is being requested
     * @return The validated integer
     */
    public static int getValidInt(Scanner scanner, String ordinal) {
        int value = 0;
        boolean isValid = false;

        System.out.println("\n----- " + ordinal.toUpperCase() + " INTEGER INPUT -----");
        System.out.println("Please enter the " + ordinal + " integer following these rules:");
        System.out.println("1. Must be a valid integer (whole number)");
        System.out.println("2. Must be within the range of a 4-byte int:");
        System.out.println("   Minimum: " + MIN_INT_VALUE + " (-2,147,483,648)");
        System.out.println("   Maximum: " + MAX_INT_VALUE + " (2,147,483,647)");

        while (!isValid) {
            System.out.print("\nEnter the " + ordinal + " integer: ");

            // Use hasNextInt to check if input is a valid integer
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                isValid = true;
            } else {
                System.out.println("Error: Input is not a valid integer. Please try again.");
                scanner.next(); // Consume the invalid input
            }
        }

        scanner.nextLine(); // Consume the newline character after reading the int
        return value;
    }

    /**
     * Prompts for and validates a file name
     * @param scanner The Scanner object for input
     * @param fileType "input" or "output" to indicate which file is being requested
     * @return The validated file name
     */
    public static String getValidFileName(Scanner scanner, String fileType) {
        String fileName = "";
        boolean isValid = false;

        System.out.println("\n----- " + fileType.toUpperCase() + " FILE NAME -----");
        System.out.println("Please enter the " + fileType + " file name following these rules:");
        System.out.println("1. Maximum " + MAX_FILENAME_LENGTH + " characters");
        System.out.println("2. Only alphanumeric characters (A-Z, a-z, 0-9), periods (.), underscores (_), and hyphens (-) are allowed");
        System.out.println("3. Cannot be empty");
        if (fileType.equals("input")) {
            System.out.println("4. File must exist - will check in current directory and src folder");
            System.out.println("   Don't forget to include the file extension (e.g., .txt)");
        }

        while (!isValid) {
            System.out.print("\nEnter the " + fileType + " file name: ");
            fileName = scanner.nextLine().trim();

            // Check if file name is empty
            if (fileName.isEmpty()) {
                System.out.println("Error: File name cannot be empty. Please try again.");
                continue;
            }

            // Check length
            if (fileName.length() > MAX_FILENAME_LENGTH) {
                System.out.println("Error: File name exceeds maximum length of " +
                        MAX_FILENAME_LENGTH + " characters. Please try again.");
                continue;
            }

            // Check character validity using regex
            Pattern pattern = Pattern.compile(FILENAME_PATTERN);
            Matcher matcher = pattern.matcher(fileName);

            if (!matcher.matches()) {
                System.out.println("Error: File name contains invalid characters. " +
                        "Only alphanumeric characters, periods, underscores, and hyphens are allowed. Please try again.");
                continue;
            }

            // For input files, check if the file exists
            if (fileType.equals("input")) {
                // Try multiple locations where the file might be
                File fileInCurrent = new File(fileName);
                File fileInSrc = new File("src/" + fileName);

                if (fileInCurrent.exists() && fileInCurrent.isFile()) {
                    // File found in current directory
                    fileName = fileInCurrent.getPath();
                    isValid = true;
                } else if (fileInSrc.exists() && fileInSrc.isFile()) {
                    // File found in src directory
                    fileName = fileInSrc.getPath();
                    isValid = true;
                } else {
                    System.out.println("Error: Input file '" + fileName + "' could not be found.");
                    System.out.println("The file was not found in either:");
                    System.out.println("  - Current working directory: " + new File(".").getAbsolutePath());
                    System.out.println("  - src folder: " + new File("src/").getAbsolutePath());
                    System.out.println("Make sure the file exists and you've included the correct file extension (e.g., .txt).");

                    // Ask if user wants to try a different path
                    System.out.print("Would you like to provide the absolute path to the file? (Y/N): ");
                    String response = scanner.nextLine().trim().toUpperCase();

                    if (response.equals("Y") || response.equals("YES")) {
                        System.out.println("\nPlease enter the absolute path to the file (e.g., C:/Users/YourName/Documents/abc.txt):");
                        continue; // Will prompt for input again
                    } else {
                        // User doesn't want to try again with absolute path
                        continue; // Will prompt for input again
                    }
                }
            } else {
                // For output files, no existence check needed
                isValid = true;
            }
        }

        return fileName;
    }
}