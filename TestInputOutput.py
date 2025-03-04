def test_file_handlers():
    # Import your classes
    from InputFile import InputFile
    from OutputFile import OutputFile

    print("===== TESTING INPUT FILE CLASS =====")
    print("We'll be using 'input.txt' that already exists in the directory")

    # Test InputFile class
    input_handler = InputFile()
    print("\nPlease enter 'input.txt' when prompted:")
    input_filename = input_handler.get_input_file()
    print(f"Input file test result: {'PASS' if input_filename == 'input.txt' else 'FAIL'}")

    # Display content of the input file
    try:
        with open(input_filename, 'r') as f:
            content = f.read()
        print(f"\nContent of input file: \n{content[:100]}..." if len(content) > 100 else content)
    except Exception as e:
        print(f"Error reading file: {e}")

    print("\n===== TESTING OUTPUT FILE CLASS =====")
    print("We'll be using 'output.txt' for our output")

    # Test OutputFile class
    output_handler = OutputFile()
    print("\nPlease enter 'output.txt' when prompted:")
    output_filename = output_handler.get_output_file()
    print(f"Output file test result: {'PASS' if output_filename == 'output.txt' else 'FAIL'}")

    # Write something to the output file to test
    try:
        with open(output_filename, 'w') as f:
            f.write("This is a test write to the output file.\n")
            f.write(f"The input came from: {input_filename}")
        print("Successfully wrote to output file!")
    except Exception as e:
        print(f"Error writing to file: {e}")

    print("\n===== TEST COMPLETE =====")
    print("Both input and output file handlers have been tested.")
    print(f"Input file: {input_filename}")
    print(f"Output file: {output_filename}")


# Run the test
if __name__ == "__main__":
    test_file_handlers()