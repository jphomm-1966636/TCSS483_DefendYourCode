
class InputFile:
    def __init__(self):
        self.filename = None
        self.allowed_extensions = ['.txt']

    def has_valid_extension(self, filename):
        """Check if the file has an allowed extension"""
        # Simple string check for extension
        valid = False
        for ext in self.allowed_extensions:
            if filename.lower().endswith(ext):
                valid = True
                break

        if not valid:
            print(f"Error: File must have one of these extensions: {', '.join(self.allowed_extensions)}")
            return False
        return True

    def is_safe_path(self, filename):
        """Check if the file is in the current directory (no path separators)"""
        # Check if the filename contains any path separators
        if '/' in filename or '\\' in filename:
            print("Error: Only files in the current directory are allowed. Do not include path separators (/ or \\).")
            return False

        return True

    def is_valid_file(self, filename):
        """Validate file exists, is readable, has valid extension and safe path"""
        # First check extension and path
        if not self.has_valid_extension(filename):
            return False

        if not self.is_safe_path(filename):
            return False

        # Then check if file exists and is readable
        try:
            with open(filename, 'r') as file:
                # Try to read a small portion to verify it's readable
                file.read(1)
                return True
        except Exception as e:
            print(f"Error: An issue occurred with file '{filename}': {str(e)}")
            return False

    def get_file_input(self, prompt):
        while True:
            filename = input(prompt)

            if self.is_valid_file(filename):
                return filename

    def get_input_file(self):
        print("\n=== INPUT FILE ===")
        print("Please enter the name of an input file.")
        print("Requirements:")
        print(f"- File must have one of these extensions: {', '.join(self.allowed_extensions)}")
        print("- File must exist in the current directory or at the specified path")
        print("- File must be readable as text")
        print("- System directories and sensitive locations cannot be accessed")

        self.filename = self.get_file_input("Enter filename: ")

        print(f"Valid input file selected: {self.filename}")
        return self.filename

#
# input_file = InputFile()
# input_file.get_input_file()

