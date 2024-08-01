// Matches names with at least three characters, including any European letters, spaces, apostrophes, and dashes
const NAME_REGEX = /^[\p{L}'][ \p{L}'-]{2,}$/u;
// Matches insurance numbers with 7 to 20 alphanumeric characters
const INSURANCE_NR_REGEX = /^[A-Za-z0-9]{7,20}$/;
// Matches phone numbers with optional country code, separators, and a minimum length of digits
const PHONE_REGEX = /^[+]?[0-9]{1,3}[ \s.-]?([0-9]{2,}[ \s.-]?){2,}[0-9]{2,}$/;
// Matches email addresses with letters, numbers, and some special characters
const EMAIL_REGEX = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;

const validation = {
    isValidName(name: string): boolean {
        return NAME_REGEX.test(name);
    },

    isValidInsuranceNumber(insuranceNr: string): boolean {
        return INSURANCE_NR_REGEX.test(insuranceNr);
    },

    isValidDateOfBirth(dateOfBirth: string): boolean {
        const today = new Date();
        const dob = new Date(dateOfBirth);
        return !isNaN(dob.getTime()) && dob < today;
    },

    isValidPhoneNumber(phoneNumber: string | undefined): boolean {
        return phoneNumber === undefined || PHONE_REGEX.test(phoneNumber);
    },

    isValidEmail(email: string | undefined): boolean {
        return email === undefined || EMAIL_REGEX.test(email);
    }
};

export default validation;