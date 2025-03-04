import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

/**
 * Handles password validation, hashing, and verification
 */
public class PasswordManager {
    // Constants for password validation
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private static final String PASSWORD_FILE = "password.hash";

    /**
     * Gets a valid password from the user, hashes it, and verifies it
     * @param scanner The Scanner object for input
     * @return The hash of the validated password
     */
    public static String getValidPasswordAndHash(Scanner scanner) {
        String password = "";
        String passwordHash = null;
        String salt = generateSalt();
        boolean isValid = false;

        System.out.println("\n----- PASSWORD ENTRY -----");
        System.out.println("Please enter a password following these rules:");
        System.out.println("1. Between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters");
        System.out.println("2. Must contain at least one digit (0-9)");
        System.out.println("3. Must contain at least one lowercase letter (a-z)");
        System.out.println("4. Must contain at least one uppercase letter (A-Z)");
        System.out.println("5. Must contain at least one special character (@#$%^&+=!)");
        System.out.println("6. No whitespace allowed");

        // First password entry and validation
        while (!isValid) {
            System.out.print("\nEnter your password: ");
            password = scanner.nextLine();

            // Check if password is empty
            if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please try again.");
                continue;
            }

            // Check length
            if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
                System.out.println("Error: Password must be between " + MIN_PASSWORD_LENGTH +
                        " and " + MAX_PASSWORD_LENGTH + " characters. Please try again.");
                continue;
            }

            // Check password pattern
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
            Matcher matcher = pattern.matcher(password);

            if (!matcher.matches()) {
                System.out.println("Error: Password does not meet the requirements. Please try again.");
                continue;
            }

            isValid = true;
        }

        // Hash the password with salt
        passwordHash = hashPassword(password, salt);

        // Save the hash and salt to file
        try {
            savePasswordHash(passwordHash, salt);
            System.out.println("Password hash saved successfully.");
        } catch (IOException e) {
            ErrorLogger.logError("Could not save password hash to file: " + e.getMessage());
            System.out.println("Warning: Could not save password hash to file. See error log for details.");
        }

        // Second password entry for verification
        boolean verified = false;
        while (!verified) {
            System.out.print("\nPlease re-enter your password for verification: ");
            String passwordVerify = scanner.nextLine();

            try {
                // Retrieve saved hash and salt
                String[] savedHashData = retrievePasswordHash();
                String savedSalt = savedHashData[0];
                String savedHash = savedHashData[1];

                // Hash the verification password with the same salt
                String verifyHash = hashPassword(passwordVerify, savedSalt);

                // Compare the hashes
                if (savedHash.equals(verifyHash)) {
                    System.out.println("Password verification successful!");
                    verified = true;
                } else {
                    System.out.println("Error: Passwords do not match. Please try again.");
                }
            } catch (IOException e) {
                ErrorLogger.logError("Could not retrieve password hash from file: " + e.getMessage());
                System.out.println("Error: Could not verify password. See error log for details.");
                // Return to the first password entry
                return getValidPasswordAndHash(scanner);
            }
        }

        return passwordHash;
    }

    /**
     * Generates a random salt for password hashing
     * @return A random salt as a hexadecimal string
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * Hashes a password with a salt using SHA-256
     * @param password The password to hash
     * @param salt The salt to use
     * @return The hashed password
     */
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hexToBytes(salt));
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            ErrorLogger.logError("Hashing algorithm not available: " + e.getMessage());
            System.out.println("Error: Hashing algorithm not available.");
            return null;
        }
    }

    /**
     * Saves the password hash and salt to a file
     * @param hash The password hash
     * @param salt The salt used
     * @throws IOException If an I/O error occurs
     */
    private static void savePasswordHash(String hash, String salt) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSWORD_FILE))) {
            writer.write(salt + ":" + hash);
        }
    }

    /**
     * Retrieves the password hash and salt from the file
     * @return Array containing [salt, hash]
     * @throws IOException If an I/O error occurs
     */
    private static String[] retrievePasswordHash() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts;
                }
            }
            throw new IOException("Invalid password hash file format");
        }
    }

    /**
     * Converts a byte array to a hexadecimal string
     * @param bytes The byte array
     * @return The hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Converts a hexadecimal string to a byte array
     * @param hex The hexadecimal string
     * @return The byte array
     */
    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}