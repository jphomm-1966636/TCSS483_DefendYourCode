import logging
import re


# Set up error logging
logging.basicConfig(filename='error_log.txt', level=logging.ERROR,
                    format='%(asctime)s - %(levelname)s - %(message)s')

class Name:
    def __init__(self):
        self.pattern = r"^[a-zA-Z][a-zA-Z' -]*[a-zA-Z]$|^[a-zA-Z]$"
        self.max_length = 50

    def is_valid(self, name):
        if not name or len(name) > self.max_length:
            return False
        return bool(re.match(self.pattern, name))

    def get_valid_name(self, prompt):
        print(f"\n{prompt}")
        print(f"Below are the rules to which you must follow when inputting")
        print(f"- Maximum length: {self.max_length} characters")
        print("- Must contain only letters, spaces, hyphens, and apostrophes")
        print("- Must start and end with a letter\n")

        while True:
            name = input("Input here > ")

            if self.is_valid(name):
                return name

            if len(name) > self.max_length:
                logging.error(f"Error: Name must be {self.max_length} characters or less.")
                print("invalid, try again")
            else:
                logging.error("Error: Invalid name format. Please try again.")
                print("invalid, try again")