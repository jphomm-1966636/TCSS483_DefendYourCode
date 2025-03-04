import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Handles file processing operations
 */
public class FileProcessor {

    /**
     * Processes the files and writes the output
     * @param firstName User's first name
     * @param lastName User's last name
     * @param firstInt First integer
     * @param secondInt Second integer
     * @param inputFileName Input file name
     * @param outputFileName Output file name
     * @param passwordHash Password hash
     * @throws IOException If an I/O error occurs
     */
    public static void processFiles(String firstName, String lastName, int firstInt, int secondInt,
                                    String inputFileName, String outputFileName, String passwordHash) throws IOException {

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(inputFileName));
            writer = new BufferedWriter(new FileWriter(outputFileName));

            // Calculate sum and product using BigInteger to avoid overflow
            BigInteger first = BigInteger.valueOf(firstInt);
            BigInteger second = BigInteger.valueOf(secondInt);
            BigInteger sum = first.add(second);
            BigInteger product = first.multiply(second);

            // Write user's name
            writer.write("First name: " + firstName);
            writer.newLine();
            writer.write("Last name: " + lastName);
            writer.newLine();
            writer.newLine();

            // Write integers and calculations
            writer.write("First Integer: " + firstInt);
            writer.newLine();
            writer.write("Second Integer: " + secondInt);
            writer.newLine();
            writer.write("Sum: " + sum);
            writer.newLine();
            writer.write("Product: " + product);
            writer.newLine();
            writer.newLine();

            // Write file information
            writer.write("Input File Name: " + inputFileName);
            writer.newLine();
            writer.newLine();

            // Write input file contents
            writer.write("Input file contents:");
            writer.newLine();
            writer.write("------------------");
            writer.newLine();

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();

                // Echo to screen
                System.out.println("Read from input file: " + line);
            }

            // Echo success to screen
            System.out.println("\nWritten to output file: " + outputFileName);

        } catch (IOException e) {
            ErrorLogger.logError("Error processing files: " + e.getMessage());
            throw e;
        } finally {
            // Close resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    ErrorLogger.logError("Error closing input file: " + e.getMessage());
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    ErrorLogger.logError("Error closing output file: " + e.getMessage());
                }
            }
        }
    }
}