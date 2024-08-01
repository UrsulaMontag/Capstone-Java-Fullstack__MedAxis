package um_backend.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class DataValidationService {
    public boolean isValidName(String name) {
        // Matches names with at least three characters, including European letters, spaces, apostrophes, and dashes
        String nameRegex = "^[\\p{L}'][ \\p{L}'-]{2,}$";
        return name != null && name.matches(nameRegex);
    }

    public boolean isValidInsuranceNumber(String insuranceNr) {
        // Matches insurance numbers with 7 to 20 alphanumeric characters
        String insuranceNrRegex = "^[A-Za-z0-9]{7,20}$";
        return insuranceNr != null && insuranceNr.matches(insuranceNrRegex);
    }

    public boolean isValidDateOfBirth(String dateOfBirth) {
        // Validates that the date of birth is a valid date and is in the past
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return false;
        }
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth);
            return dob.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        // Matches phone numbers with optional country code, separators, and minimum digit lengths
        String phoneNumberRegex = "^[+]?\\d{1,3}[\\s.-]?\\d{2,4}([\\s.-]?\\d{2,4}){1,3}$";
        return phoneNumber == null || phoneNumber.matches(phoneNumberRegex);
    }

    public boolean isValidEmail(String email) {
        // Matches email addresses with standard formatting
        String emailRegex = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+$";
        return email == null || email.matches(emailRegex);
    }

}

