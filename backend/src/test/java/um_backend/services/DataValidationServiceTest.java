package um_backend.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DataValidationServiceTest {

    private final DataValidationService dataValidationService = new DataValidationService();

    @Test
    void isValidName_testsIfNameInputIsValid() {
        assertThat(dataValidationService.isValidName("John Doe")).isTrue();
        assertThat(dataValidationService.isValidName("John123")).isFalse(); // Contains digits
        assertThat(dataValidationService.isValidName("O'Connor")).isTrue(); // Valid name with apostrophe
        assertThat(dataValidationService.isValidName("Jean-Pierre")).isTrue(); // Valid name with dash
        assertThat(dataValidationService.isValidName("José Silva")).isTrue(); // European characters
        assertThat(dataValidationService.isValidName("")).isFalse(); // Empty string
        assertThat(dataValidationService.isValidName("J")).isFalse(); // Too short
        assertThat(dataValidationService.isValidName(null)).isFalse(); // Null input
    }

    @Test
    void isValidInsuranceNumber_testsIfInsuranceNrInputIsValid() {
        assertThat(dataValidationService.isValidInsuranceNumber("A123456789")).isTrue();
        assertThat(dataValidationService.isValidInsuranceNumber("123")).isFalse(); // Less than 7 characters
        assertThat(dataValidationService.isValidInsuranceNumber("A123456789012345678901")).isFalse(); // More than 20 characters
        assertThat(dataValidationService.isValidInsuranceNumber("A12 3456789")).isFalse(); // Contains space
        assertThat(dataValidationService.isValidInsuranceNumber(null)).isFalse(); // Null input
    }

    @Test
    void isValidDateOfBirth_testsIfDateOfBirthInputIsValid() {
        assertThat(dataValidationService.isValidDateOfBirth("1990-01-01")).isTrue();
        assertThat(dataValidationService.isValidDateOfBirth("3000-01-01")).isFalse(); // Future date
        assertThat(dataValidationService.isValidDateOfBirth("")).isFalse(); // Empty string
        assertThat(dataValidationService.isValidDateOfBirth(null)).isFalse(); // Null input
        assertThat(dataValidationService.isValidDateOfBirth("invalid-date")).isFalse(); // Invalid date format
    }

    @Test
    void isValidPhoneNumber_testsIfPhoneNumberInputIsValid() {
        assertThat(dataValidationService.isValidPhoneNumber("+1-800-123-4567")).isTrue(); // Valid format
        assertThat(dataValidationService.isValidPhoneNumber("+49 123 456 7890")).isTrue(); // Valid with space separators
        assertThat(dataValidationService.isValidPhoneNumber("0049 1234-567890")).isTrue(); // Valid without +
        assertThat(dataValidationService.isValidPhoneNumber("123-456-7890")).isTrue(); // Valid local number
        assertThat(dataValidationService.isValidPhoneNumber("1-800-123-456789012345")).isFalse(); // Too long
        assertThat(dataValidationService.isValidPhoneNumber("+1 (800) 123-4567")).isFalse(); // Invalid characters
        assertThat(dataValidationService.isValidPhoneNumber(null)).isTrue(); // Null input, acceptable per current logic
    }

    @Test
    void isValidEmail_testsIfEmailInputIsValid() {
        assertThat(dataValidationService.isValidEmail("test@example.com")).isTrue(); // Valid email
        assertThat(dataValidationService.isValidEmail("user.name+tag+sorting@example.com")).isTrue(); // Valid with special characters
        assertThat(dataValidationService.isValidEmail("test.email@sub.domain.com")).isTrue(); // Valid with subdomain
        assertThat(dataValidationService.isValidEmail("invalid-email")).isFalse(); // Missing @
        assertThat(dataValidationService.isValidEmail("test@.com")).isFalse(); // Missing domain name
        assertThat(dataValidationService.isValidEmail("test@domain.c")).isTrue(); // Single character top-level domain is valid
        assertThat(dataValidationService.isValidEmail(null)).isTrue(); // Null input, acceptable per current logic
    }
}