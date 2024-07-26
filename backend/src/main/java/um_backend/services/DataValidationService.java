package um_backend.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataValidationService {
    public boolean isValidName(String name) {
        String nameRegex = "^[A-Za-z\\säöüÄÖÜß'-]+$";
        return name != null && name.matches(nameRegex);
    }

    public boolean isValidInsuranceNumber(String insuranceNr) {
        String insuranceNrRegex = "^[A-Za-z0-9]{7,20}$";
        return insuranceNr != null && insuranceNr.matches(insuranceNrRegex);
    }

    public boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth != null && dateOfBirth.isBefore(LocalDate.now());
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[+]?[0-9]{1,3}?[\\s.-]?[(]?[0-9]{1,4}[)]?[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$";
        return phoneNumber == null || phoneNumber.matches(phoneNumberRegex);
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email == null || email.matches(emailRegex);
    }
}

