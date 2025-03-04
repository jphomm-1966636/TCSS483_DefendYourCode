import logging

# Set up error logging
logging.basicConfig(filename='error_log.txt', level=logging.ERROR,
                    format='%(asctime)s - %(levelname)s - %(message)s')

class OutputFile:
    def __init__(self, input_file=None):
        self.filename = None
        self.allowed_extensions = ['.txt']
        self.input_file = input_file

    def has_valid_extension(self, filename):
        """Check if the file has an allowed extension"""
        # Simple string check for extension
        valid = False
        for ext in self.allowed_extensions:
            if filename.lower().endswith(ext):
                valid = True
                break

        if not valid:
            logging.error(f"Error: File must have one of these extensions: {', '.join(self.allowed_extensions)}")
            print("invalid, try again")
            return False
        return True

    def is_safe_path(self, filename):
        """Check if the file is in the current directory (no path separators)"""
        # Check if the filename contains any path separators
        if '/' in filename or '\\' in filename:
            logging.error("Error: Only files in the current directory are allowed. Do not include path separators (/ or \\).")
            print("invalid, try again")
            return False

        return True

    def is_different_from_input(self, filename):
        """Check if the output file is different from the input file"""
        if self.input_file and filename.lower() == self.input_file.lower():
            logging.error(f"Error: Output file cannot be the same as input file '{self.input_file}'")
            print("invalid, try again")
            return False
        return True

    def is_valid_file(self, filename):
        """Validate output file can be created and written to"""
        # First check extension and path
        if not self.has_valid_extension(filename):
            return False

        if not self.is_safe_path(filename):
            return False

        # Check if output is different from input
        if not self.is_different_from_input(filename):
            return False

        # Check if we can write to this location
        try:
            # Try to open the file in write mode briefly to ensure we have permission
            with open(filename, 'w') as file:
                pass
            return True
        except Exception as e:
            logging.error(f"Error: An issue occurred with file '{filename}': {str(e)}")
            print("error, try again")
            return False

    def get_file_input(self, prompt):
        while True:
            filename = input(prompt)

            if self.is_valid_file(filename):
                return filename

    def get_output_file(self):
        print("\n=== OUTPUT FILE ===")
        print("Please enter the name of an output file.")
        print("Requirements:")
        print(f"- File must have one of these extensions: {', '.join(self.allowed_extensions)}")
        print("- File must be in the current directory")
        print("- File must be writable")
        print("- If the file already exists, it will be overwritten")

        self.filename = self.get_file_input("Enter filename: ")

        print(f"Valid output file selected: {self.filename}")
        return self.filename

#
# output_file = OutputFile()
# output_file.get_output_file()