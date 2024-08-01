const NAME_REGEX = /^[A-Za-z\säöüÄÖÜß'-]+$/;
const INSURANCE_NR_REGEX = /^[A-Za-z0-9]{7,20}$/;
const PHONE_REGEX = /^[+]?[0-9]{1,3}?[\\s.-]?[(]?[0-9]{1,4}[)]?[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$/;
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