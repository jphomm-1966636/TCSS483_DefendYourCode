import re
import random
import os


class Password:
    def __init__(self):
        self.password_file = "password.txt"
        self.min_length = 8
        self.max_length = 50
        # Regex pattern requiring:
        # - 8-50 characters
        # - At least one uppercase letter
        # - At least one lowercase letter
        # - At least one number
        # - At least one special character
        self.password_pattern = r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_\-+=<>?/\[\]{}]).{8,50}$'

        # Create new file or delete existing one and create new
        self._initialize_password_file()

    def _initialize_password_file(self):
        """Delete the existing password file if it exists and create a new one"""
        if os.path.exists(self.password_file):
            try:
                os.remove(self.password_file)
                # Create an empty file
                open(self.password_file, 'w').close()
            except Exception as e:
                print(f"Error initializing password file: {e}")
        else:
            # Create an empty file
            try:
                open(self.password_file, 'w').close()
            except Exception as e:
                print(f"Error creating password file: {e}")

    def is_valid_password(self, password):
        """Check if password meets all requirements using regex"""
        if re.match(self.password_pattern, password):
            return True
        return False

    def get_password_requirements(self):
        """Return a string describing password requirements"""
        return (f"Password must be {self.min_length}-{self.max_length} characters and contain:\n"
                f"- At least one uppercase letter (A-Z)\n"
                f"- At least one lowercase letter (a-z)\n"
                f"- At least one number (0-9)\n"
                f"- At least one special character (!@#$%^&*()_-+=<>?/[]{{}})")

    def hash_password(self, password):
        """Create a hash of the password with a salt"""
        # Generate a simple salt (a random number)
        salt = str(random.randint(1000, 9999))

        # Hash the password with the salt
        hashed_password = str(hash(password + salt))

        return {
            'salt': salt,
            'hash': hashed_password
        }

    def save_password_hash(self, hash_data):
        """Save the salt and hashed password to a file"""
        try:
            with open(self.password_file, 'w') as file:
                file.write(f"{hash_data['salt']}\n{hash_data['hash']}")
            return True
        except Exception as e:
            print(f"Error saving password: {e}")
            return False

    def verify_password(self, password):
        """Verify a password against the stored hash"""
        try:
            # Read the stored salt and hash
            with open(self.password_file, 'r') as file:
                stored_salt = file.readline().strip()
                stored_hash = file.readline().strip()

            # Hash the provided password with the same salt
            hash_to_verify = str(hash(password + stored_salt))

            # Compare the generated hash with the stored hash
            return hash_to_verify == stored_hash

        except Exception as e:
            print(f"Error verifying password: {e}")
            return False

    def get_password(self):
        """Prompt user for a password, validate, hash, and store it"""
        print(self.get_password_requirements())

        while True:
            password = input("Enter a password: ")

            if not self.is_valid_password(password):
                print("Invalid password. Please ensure it meets all requirements.")
                continue

            # Hash and store the password
            hash_data = self.hash_password(password)
            if not self.save_password_hash(hash_data):
                print("Error storing password. Please try again.")
                continue

            # Verify the password
            print("Please re-enter your password to verify.")
            while True:
                verify_password = input("Re-enter password: ")

                if self.verify_password(verify_password):
                    print("Password verified successfully.")
                    return password
                else:
                    print("Passwords do not match. Please try again.")