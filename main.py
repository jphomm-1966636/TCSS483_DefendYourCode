"""
Defend Your Code
Team Members:
- Bill Marban
- Yonas Omega
- Jay Phommakhot

This program runs all the programs for Defend Your Code in eaxh section
"""

import logging
from name import Name
from twoints import TwoNumbers
from InputFile import InputFile
from OutputFile import OutputFile
from password import Password

# Set up error logging
logging.basicConfig(filename='error_log.txt', level=logging.ERROR,
                    format='%(asctime)s - %(levelname)s - %(message)s')


def main():
    try:
        print("=== Secure Data Processing Program ===")

        # Get user's name
        print("testing from main in here")
        name_validator = Name()
        first_name = name_validator.get_valid_name("Please enter your first name:")
        last_name = name_validator.get_valid_name("Please enter your last name:")

        # Get two integers
        number_handler = TwoNumbers()
        first_num, second_num = number_handler.get_two_numbers()

        # Calculate sum and product (with overflow check)
        sum_result = first_num + second_num
        if (first_num > 0 and second_num > 0 and sum_result < 0) or \
                (first_num < 0 and second_num < 0 and sum_result > 0):
            print("Error: Integer overflow occurred during addition")
            logging.error("Integer overflow occurred during addition")
            return

        # Python treats ints as if they're objects, so it'll never actually catch if it's four bytes
        if sum_result > number_handler.four_bytes_max or sum_result < number_handler.four_bytes_min:
            print("Error: Integer overflow occurred during addition, went past four bytes")
            logging.error("Integer overflow occurred during addition")
            return

        # Check for potential multiplication overflow
        # Python handles large integers automatically, but we'll verify against 4-byte int limits
        product_result = first_num * second_num
        if product_result > number_handler.four_bytes_max or product_result < number_handler.four_bytes_min:
            print("Error: Integer overflow occurred during multiplication")
            logging.error("Integer overflow occurred during multiplication")
            return

        # Get input file
        input_handler = InputFile()
        input_filename = input_handler.get_input_file()

        # Get output file
        output_handler = OutputFile(input_filename)
        output_filename = output_handler.get_output_file()

        # Get password
        password_manager = Password()
        password = password_manager.get_password()

        # Read contents from input file
        try:
            with open(input_filename, 'r') as infile:
                input_file_contents = infile.read()
        except Exception as e:
            print(f"Error reading input file: {e}")
            logging.error(f"Error reading input file: {e}")
            return

        # Write all required information to output file
        try:
            with open(output_filename, 'w') as outfile:
                # Write user's name
                outfile.write("=== USER INFORMATION ===\n")
                outfile.write(f"First Name: {first_name}\n")
                outfile.write(f"Last Name: {last_name}\n\n")

                # Write integer values and calculations
                outfile.write("=== INTEGER CALCULATIONS ===\n")
                outfile.write(f"First Integer: {first_num}\n")
                outfile.write(f"Second Integer: {second_num}\n")
                outfile.write(f"Sum: {sum_result}\n")
                outfile.write(f"Product: {product_result}\n\n")

                # Write file information
                outfile.write("=== FILE INFORMATION ===\n")
                outfile.write(f"Input File Name: {input_filename}\n\n")
                outfile.write("Input File Contents:\n")
                outfile.write("----------------------\n")
                outfile.write(input_file_contents)
                outfile.write("\n----------------------\n")

            print(f"\nAll information has been successfully written to {output_filename}")

            # Echo data to screen if desired
            print("\n=== OUTPUT SUMMARY (also written to file) ===")
            print(f"Name: {first_name} {last_name}")
            print(f"Integers: {first_num} and {second_num}")
            print(f"Sum: {sum_result}")
            print(f"Product: {product_result}")
            print(f"Input file: {input_filename}")
            print("Input file contents were successfully processed")

        except Exception as e:
            print(f"Error writing to output file: {e}")
            logging.error(f"Error writing to output file: {e}")
            return

        print("\nProgram executed successfully!")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        logging.error(f"Unexpected error: {e}")


if __name__ == "__main__":
    main()