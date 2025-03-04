import re


class Name:
    def __init__(self):
        self.pattern = r"^[a-zA-Z][a-zA-Z' -]*[a-zA-Z]$|^[a-zA-Z]$"
        self.max_length = 50

    def is_valid(self, name):
        """
        Method that returns true or false
        :param name: The name string to validate
        :return: True if name follows the pattern and length requirements
        """
        if not name or len(name) > self.max_length:
            return False
        return bool(re.match(self.pattern, name))

    def get_valid_name(self, prompt):
        """
        Get a valid name from the user
        :param prompt: The message to display to the user
        :return: A valid name
        """
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
                print(f"Error: Name must be {self.max_length} characters or less.")
            else:
                print("Error: Invalid name format. Please try again.")


# Create name validator and get first and last names
# name_validator = Name()
# first_name = name_validator.get_valid_name("Please enter your first name:")
# last_name = name_validator.get_valid_name("Please enter your last name:")
# print(f"Thank you for following the rules, {first_name} {last_name}")