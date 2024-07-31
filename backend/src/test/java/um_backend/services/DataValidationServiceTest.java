package um_backend.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DataValidationServiceTest {

    private final DataValidationService dataValidationService = new DataValidationService();

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
        assertThat(dataValidationService.isValidDateOfBirth("1990-01-01")).isTrue();
        assertThat(dataValidationService.isValidDateOfBirth("3000-01-01")).isFalse();
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