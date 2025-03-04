import logging

# Set up error logging
logging.basicConfig(filename='error_log.txt', level=logging.ERROR,
                    format='%(asctime)s - %(levelname)s - %(message)s')

class TwoNumbers:
    def __init__(self):
        self.four_bytes_max = 2147483647
        self.four_bytes_min = -2147483648
        self.first_number = None
        self.second_number = None

    def is_valid_number(self, user_input):
        try:
            num = int(user_input)
            return True
        except ValueError:
            return False

    def is_within_range(self, num):
        if num > self.four_bytes_max or num < self.four_bytes_min:
            return False
        return True

    def get_integer_input(self, prompt):
        while True:
            user_input = input(prompt)

            if not self.is_valid_number(user_input):
                logging.error("Error: Please enter a valid integer.")
                print("invalid, try again")
                continue

            num = int(user_input)

            if not self.is_within_range(num):
                logging.error(f"Error: Number must be between {self.four_bytes_min} and {self.four_bytes_max}.")
                print("invalid, try again")
                continue

            return num

    def get_two_numbers(self):
        print(f"Please enter two integers between {self.four_bytes_min} and {self.four_bytes_max}.")

        self.first_number = self.get_integer_input("Enter first integer: ")
        self.second_number = self.get_integer_input("Enter second integer: ")

        print(f"You entered: {self.first_number} and {self.second_number}")
        return self.first_number, self.second_number

#
# number = TwoNumbers()
# number.get_two_numbers()

