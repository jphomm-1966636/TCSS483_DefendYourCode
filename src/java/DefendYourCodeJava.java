import java.util.Scanner;
import java.io.*;
import java.security.*;
import java.math.BigInteger;
import java.util.regex.*;

public class DefendYourCodeJava {

    // Constants for validation
    private static final int MAX_NAME_LENGTH = 50;
    private static final String PASSWORD_FILE = "password.hash";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB max file size

    public static void main(String[] args) {
        ErrorLogger.init();

        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("Welcome to the user input program!");

            // Get user's name
            String firstName = getName(scan, "first");
            String lastName = getName(scan, "last");

            // Get integers
            int num1 = getInteger(scan, "first");
            int num2 = getInteger(scan, "second");

            // Get filenames
            String inputFile = getFileName(scan, "input");
            String outputFile = getFileName(scan, "output");

            // Get password
            String hash = getPassword(scan);

            // Process files
            writeToFile(firstName, lastName, num1, num2, inputFile, outputFile, hash);

            System.out.println("\nProgram completed successfully!");

        } catch (Exception e) {
            ErrorLogger.logError("Program error", e);
            System.out.println("An error occurred. See error_log.txt for details.");
        } finally {
            scan.close();
        }
    }

    // Get and validate name
    private static String getName(Scanner scan, String nameType) {
        String name = "";
        boolean valid = false;

        System.out.println("\nEnter your " + nameType + " name:");
        System.out.println("- Maximum 50 characters");
        System.out.println("- Only letters, hyphens, apostrophes, and spaces allowed");

        while (!valid) {
            System.out.print("> ");
            name = scan.nextLine().trim();

            if (name.isEmpty()) {
                String errorMsg = nameType + " name validation error: Name was empty";
                ErrorLogger.logError(errorMsg);
                System.out.println("Name cannot be empty. Try again.");
            } else if (name.length() > MAX_NAME_LENGTH) {
                String errorMsg = nameType + " name validation error: Name exceeded max length (" + name.length() + " > " + MAX_NAME_LENGTH + ")";
                ErrorLogger.logError(errorMsg);
                System.out.println("Name is too long (max 50 characters). Try again.");
            } else if (!name.matches("[a-zA-Z\\-'\\s]+")) {
                String errorMsg = nameType + " name validation error: Name contained invalid characters: " + name;
                ErrorLogger.logError(errorMsg);
                System.out.println("Name contains invalid characters. Try again.");
            } else {
                valid = true;
            }
        }

        return name;
    }

    // Get and validate integer
    private static int getInteger(Scanner scan, String which) {
        int num = 0;
        boolean valid = false;

        System.out.println("\nEnter the " + which + " integer:");
        System.out.println("- Must be a whole number between -2,147,483,648 and 2,147,483,647");

        while (!valid) {
            System.out.print("> ");

            if (scan.hasNextInt()) {
                num = scan.nextInt();
                valid = true;
            } else {
                String input = scan.next(); // Get the invalid input
                String errorMsg = which + " integer validation error: Input was not a valid integer: " + input;
                ErrorLogger.logError(errorMsg);
                System.out.println("Not a valid integer. Try again.");
            }
        }

        scan.nextLine(); // Consume newline
        return num;
    }

    // Get and validate filename
    private static String getFileName(Scanner scan, String type) {
        String fileName = "";
        boolean valid = false;

        System.out.println("\nEnter " + type + " file name:");
        if (type.equals("input")) {
            System.out.println("- File must exist and be less than 10MB");
            System.out.println("- Include file extension (like .txt)");
        } else {
            System.out.println("- Output file will be created or overwritten");
            System.out.println("- Path must be writable");
        }

        while (!valid) {
            System.out.print("> ");
            fileName = scan.nextLine().trim();

            if (fileName.isEmpty()) {
                String errorMsg = type + " file validation error: Filename was empty";
                ErrorLogger.logError(errorMsg);
                System.out.println("File name cannot be empty. Try again.");
                continue;
            }

            // For input files, check if file exists
            if (type.equals("input")) {
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    if (file.length() > MAX_FILE_SIZE) {
                        String errorMsg = "Input file validation error: File too large (" + file.length() + " bytes): " + fileName;
                        ErrorLogger.logError(errorMsg);
                        System.out.println("File is too large (>10MB). Try again.");
                        continue;
                    }
                    valid = true;
                } else {
                    // Also check in src folder
                    file = new File("src/" + fileName);
                    if (file.exists() && file.isFile()) {
                        if (file.length() > MAX_FILE_SIZE) {
                            String errorMsg = "Input file validation error: File too large (" + file.length() + " bytes): src/" + fileName;
                            ErrorLogger.logError(errorMsg);
                            System.out.println("File is too large (>10MB). Try again.");
                            continue;
                        }
                        fileName = "src/" + fileName;
                        valid = true;
                    } else {
                        String errorMsg = "Input file validation error: File not found: " + fileName;
                        ErrorLogger.logError(errorMsg);
                        System.out.println("File not found. Try again.");
                    }
                }
            } else {
                // For output files, check if path is writable
                try {
                    File file = new File(fileName);
                    File parentDir = file.getParentFile();

                    // If file doesn't have a parent directory, it's in the current directory
                    if (parentDir != null && !parentDir.exists()) {
                        String errorMsg = "Output file validation error: Directory doesn't exist: " + parentDir.getPath();
                        ErrorLogger.logError(errorMsg);
                        System.out.println("Output directory doesn't exist. Try again.");
                        continue;
                    }

                    // Try to create a test file to verify permissions
                    if (file.exists()) {
                        if (!file.canWrite()) {
                            String errorMsg = "Output file validation error: Permission denied for: " + fileName;
                            ErrorLogger.logError(errorMsg);
                            System.out.println("Cannot write to this file (permission denied). Try again.");
                            continue;
                        }
                    } else {
                        try {
                            if (!file.createNewFile()) {
                                String errorMsg = "Output file validation error: Cannot create file: " + fileName;
                                ErrorLogger.logError(errorMsg);
                                System.out.println("Cannot create output file. Try again.");
                                continue;
                            }
                            file.delete(); // Clean up test file
                        } catch (IOException e) {
                            String errorMsg = "Output file validation error: IO exception: " + e.getMessage();
                            ErrorLogger.logError(errorMsg, e);
                            System.out.println("Cannot create output file: " + e.getMessage() + ". Try again.");
                            continue;
                        }
                    }
                    valid = true;
                } catch (SecurityException e) {
                    String errorMsg = "Output file validation error: Security exception: " + e.getMessage();
                    ErrorLogger.logError(errorMsg, e);
                    System.out.println("Permission denied: " + e.getMessage() + ". Try again.");
                }
            }
        }

        return fileName;
    }

    // Get, hash, and verify password
    private static String getPassword(Scanner scan) {
        String password;
        String salt = generateSalt();
        String hash = "";
        boolean valid = false;

        System.out.println("\nCreate a password:");
        System.out.println("- At least 8 characters");
        System.out.println("- Include uppercase, lowercase, number, and special character");
        System.out.println("- Special characters allowed: !@#$%^&*()-_=+");

        while (!valid) {
            System.out.print("> ");
            password = scan.nextLine();

            if (password.length() < 8) {
                String errorMsg = "Password validation error: Password too short (" + password.length() + " chars)";
                ErrorLogger.logError(errorMsg);
                System.out.println("Password too short. Try again.");
            } else if (!password.matches(".*[A-Z].*")) {
                String errorMsg = "Password validation error: Missing uppercase letter";
                ErrorLogger.logError(errorMsg);
                System.out.println("Need at least one uppercase letter. Try again.");
            } else if (!password.matches(".*[a-z].*")) {
                String errorMsg = "Password validation error: Missing lowercase letter";
                ErrorLogger.logError(errorMsg);
                System.out.println("Need at least one lowercase letter. Try again.");
            } else if (!password.matches(".*[0-9].*")) {
                String errorMsg = "Password validation error: Missing number";
                ErrorLogger.logError(errorMsg);
                System.out.println("Need at least one number. Try again.");
            } else if (!password.matches(".*[!@#$%^&*()-_=+].*")) {
                String errorMsg = "Password validation error: Missing special character";
                ErrorLogger.logError(errorMsg);
                System.out.println("Need at least one special character. Try again.");
            } else {
                hash = hashPassword(password, salt);

                try {
                    // Save hash to file
                    FileWriter fw = new FileWriter(PASSWORD_FILE);
                    fw.write(salt + ":" + hash);
                    fw.close();

                    valid = true;
                } catch (IOException e) {
                    ErrorLogger.logError("Error saving password", e);
                    System.out.println("Error saving password. See error_log.txt for details.");
                }
            }
        }

        // Verify password
        boolean verified = false;
        System.out.println("\nVerify your password:");

        while (!verified) {
            System.out.print("> ");
            String verify = scan.nextLine();

            String verifyHash = hashPassword(verify, salt);
            if (hash.equals(verifyHash)) {
                System.out.println("Password verified!");
                verified = true;
            } else {
                String errorMsg = "Password verification failed: Passwords don't match";
                ErrorLogger.logError(errorMsg);
                System.out.println("Passwords don't match. Try again.");
            }
        }

        return hash;
    }

    // Process files
    private static void writeToFile(String firstName, String lastName, int num1, int num2,
                                    String inputFile, String outputFile, String hash) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // Calculate with BigInteger to avoid overflow
            BigInteger a = BigInteger.valueOf(num1);
            BigInteger b = BigInteger.valueOf(num2);
            BigInteger sum = a.add(b);
            BigInteger product = a.multiply(b);

            // Open files
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(outputFile));

            // Write to output file
            writer.write("First name: " + firstName + "\n");
            writer.write("Last name: " + lastName + "\n\n");

            writer.write("First integer: " + num1 + "\n");
            writer.write("Second integer: " + num2 + "\n");
            writer.write("Sum: " + sum + "\n");
            writer.write("Product: " + product + "\n\n");

            writer.write("Input file: " + inputFile + "\n\n");

            writer.write("Input file contents:\n");
            writer.write("-------------------\n");

            String line;
            int lineCount = 0;
            int maxLines = 1000; // Limit to prevent excessive printing

            while ((line = reader.readLine()) != null) {
                // Truncate very long lines for the output file
                if (line.length() > 1000) {
                    writer.write(line.substring(0, 1000) + "... [line truncated, too long]\n");
                } else {
                    writer.write(line + "\n");
                }

                // Echo to console with limitations
                lineCount++;
                if (lineCount <= maxLines) {
                    if (line.length() > 100) {
                        System.out.println("Read: " + line.substring(0, 100) + "... [truncated]");
                    } else {
                        System.out.println("Read: " + line);
                    }
                } else if (lineCount == maxLines + 1) {
                    System.out.println("... [additional lines not displayed]");
                }
            }

            System.out.println("\nOutput successfully written to " + outputFile);

        } catch (IOException e) {
            ErrorLogger.logError("File processing error", e);
            System.out.println("Error processing files. Check error_log.txt for details.");
        } finally {
            // Close resources properly
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                ErrorLogger.logError("Error closing input file", e);
            }

            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                ErrorLogger.logError("Error closing output file", e);
            }
        }
    }

    // Helper methods for password handling
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hexToBytes(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            ErrorLogger.logError("Hash error", e);
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }
}