package um_backend.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataValidationServiceTest {

    @Autowired
    private DataValidationService dataValidationService;

    @Test
    void isValidName_testsIfNameInputIsValid() {
        assertThat(dataValidationService.isValidName("John Doe")).isTrue();
        assertThat(dataValidationService.isValidName("John123")).isFalse();
    }

    @Test
    void isValidInsuranceNumber_testsIfInsuranceNrInputIsValid() {
        assertThat(dataValidationService.isValidInsuranceNumber("A123456789")).isTrue();
        assertThat(dataValidationService.isValidInsuranceNumber("123")).isFalse();
    }

    @Test
    void isValidDateOfBirth_testsIfDateOfBirthInputIsValid() {
        assertThat(dataValidationService.isValidDateOfBirth(LocalDate.of(1990, 1, 1))).isTrue();
        assertThat(dataValidationService.isValidDateOfBirth(LocalDate.of(3000, 1, 1))).isFalse();
    }

    @Test
    void isValidPhoneNumber_testsIfPhoneNumberInputIsValid() {
        assertThat(dataValidationService.isValidPhoneNumber("+1-800-123-4567")).isTrue();
        assertThat(dataValidationService.isValidPhoneNumber("123")).isFalse();
    }

    @Test
    void isValidEmail_testsIfEmailInputIsValid() {
        assertThat(dataValidationService.isValidEmail("test@example.com")).isTrue();
        assertThat(dataValidationService.isValidEmail("invalid-email")).isFalse();
    }
}