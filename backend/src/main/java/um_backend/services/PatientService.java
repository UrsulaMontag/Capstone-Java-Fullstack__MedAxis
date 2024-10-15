package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.*;
import um_backend.models.dto.HealthDataDto;
import um_backend.models.dto.PatientPersonalDTO;
import um_backend.repository.PatientRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UtilService utilService;
    private final DataValidationService dataValidationService;
    private final EncryptionService encryptionService;
    private final HealthDataService healthDataService;

    public List<Patient> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::decryptPatient)
                .toList();
    }

    public Patient getPatientById(String id) throws InvalidIdException {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
        return decryptPatient(patient);
    }

    public Patient createPatient(PatientPersonalDTO patient) throws IllegalArgumentException {
        if (patient == null) {
            throw new IllegalArgumentException("PatientPersonalDTO cannot be null");
        }
        if (utilService == null || encryptionService == null || patientRepository == null) {
            throw new IllegalStateException("Required services are not initialized");
        }
        validatePatientData(patient);
        Patient newPatient = createOrUpdatePatient(patient, null);
        patientRepository.save(newPatient);
        return decryptPatient(newPatient);
    }

    public Patient updatePatientById(String id, PatientPersonalDTO patient) throws InvalidIdException {
        validatePatientData(patient);
        Patient currentPatient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
        Patient updatedPatient = createOrUpdatePatient(patient, currentPatient);
        patientRepository.save(updatedPatient);
        return decryptPatient(updatedPatient);
    }

    public void deletePatientById(String id) throws InvalidIdException {
        if (patientRepository.existsById(id)) patientRepository.deleteById(id);
        else throw new InvalidIdException("Patient with id " + id + " not found!");
    }

    protected void validatePatientData(PatientPersonalDTO patient) {
        if (!dataValidationService.isValidName(patient.firstname()) ||
                !dataValidationService.isValidName(patient.lastname())) {
            throw new IllegalArgumentException("Invalid name format.");
        }
        if (!dataValidationService.isValidInsuranceNumber(patient.insuranceNr())) {
            throw new IllegalArgumentException("Invalid insurance number format.");
        }
        if (!dataValidationService.isValidDateOfBirth(patient.dateOfBirth())) {
            throw new IllegalArgumentException("Invalid date of birth.");
        }
        if (patient.contactInformation().phoneNr() != null && !patient.contactInformation().phoneNr().isEmpty()) {
            if (!dataValidationService.isValidPhoneNumber(patient.contactInformation().phoneNr())) {
                throw new IllegalArgumentException("Invalid phone number format.");
            }
        }
        if (patient.contactInformation().email() != null && !patient.contactInformation().email().isEmpty()) {
            if (!dataValidationService.isValidEmail(patient.contactInformation().email())) {
                throw new IllegalArgumentException("Invalid email address format.");
            }
        }
        if (!dataValidationService.isValidGender(patient.gender())) {
            throw new IllegalArgumentException("Invalid gender format.");
        }
        if (!dataValidationService.isValidNationality(patient.nationality())) {
            throw new IllegalArgumentException("Invalid nationality format.");
        }
        if (!dataValidationService.isValidMaritalStatus(patient.maritalStatus())) {
            throw new IllegalArgumentException("Invalid marital status format.");
        }
        if (!dataValidationService.isValidPrimaryLanguage(patient.primaryLanguage())) {
            throw new IllegalArgumentException("Invalid language format.");
        }
        if (!dataValidationService.isValidOccupation(patient.occupation())) {
            throw new IllegalArgumentException("Invalid occupation format.");
        }
        if (!dataValidationService.isValidEmergencyContact(patient.emergencyContact())) {
            throw new IllegalArgumentException("Invalid emergency contact format.");
        }
    }

    protected Patient decryptPatient(Patient patient) {
        String decryptedFirstname = encryptionService.decrypt(patient.firstname());
        String decryptedLastname = encryptionService.decrypt(patient.lastname());
        String decryptedDateOfBirth = encryptionService.decrypt(patient.dateOfBirth());
        String decryptedInsuranceNr = encryptionService.decrypt(patient.insuranceNr());
        String decryptedHealthDataId = encryptionService.decrypt(patient.healthDataId());
        String decryptedGender = encryptionService.decrypt(patient.gender());
        EmergencyContact decryptdEmergencyContact = new EmergencyContact(
                encryptionService.decrypt(patient.emergencyContact().name()),
                encryptionService.decrypt(patient.emergencyContact().relationship()),
                encryptionService.decrypt(patient.emergencyContact().phoneNumber())
        );
        String decryptedNationality = encryptionService.decrypt(patient.nationality());
        String decryptedMaritalStatus = encryptionService.decrypt(patient.maritalStatus());
        String decryptedPrimaryLanguage = encryptionService.decrypt(patient.primaryLanguage());
        String decryptedOccupation = encryptionService.decrypt(patient.occupation());


        ContactInformation decryptedContactInformation = new ContactInformation(
                (!patient.contactInformation().phoneNr().isEmpty() ?
                        encryptionService.decrypt(patient.contactInformation().phoneNr()) : ""),
                (!patient.contactInformation().email().isEmpty() ?
                        encryptionService.decrypt(patient.contactInformation().email()) : ""),
                encryptionService.decrypt(patient.contactInformation().address()),
                encryptionService.decrypt(patient.contactInformation().town())
        );

        return new Patient(patient.id(), decryptedFirstname, decryptedLastname,
                decryptedDateOfBirth, decryptedGender, decryptdEmergencyContact, decryptedNationality,
                decryptedMaritalStatus, decryptedPrimaryLanguage, decryptedOccupation, decryptedInsuranceNr,
                decryptedContactInformation, decryptedHealthDataId);

    }

    protected Patient createOrUpdatePatient(PatientPersonalDTO dto, Patient existingPatient) {
        String encryptedFirstName = encryptionService.encrypt(dto.firstname());
        String encryptedLastName = encryptionService.encrypt(dto.lastname());
        String encryptedDateOfBirth = encryptionService.encrypt(dto.dateOfBirth());
        String encryptedGender = encryptionService.encrypt(dto.gender());
        EmergencyContact encryptedEmergencyContact = new EmergencyContact(encryptionService.encrypt(dto.emergencyContact().name()),
                encryptionService.encrypt(dto.emergencyContact().relationship()), encryptionService.encrypt(dto.emergencyContact().phoneNumber()));

        String encryptedInsuranceNr = encryptionService.encrypt(dto.insuranceNr());
        String encryptedNationality = encryptionService.encrypt(dto.nationality());
        String encryptedMaritalStatus = encryptionService.encrypt(dto.maritalStatus());
        String encryptedPrimaryLanguage = encryptionService.encrypt(dto.primaryLanguage());
        String encryptedOccupation = encryptionService.encrypt(dto.occupation());
        ContactInformation contactInfo = createEncryptedContactInformation(dto.contactInformation());

        if (existingPatient == null) {
            // Creating a new patient
            String encryptedHealthDataId = encryptionService.encrypt(
                    createEmptyHealthDataObject(dto.gender(), calculateAgeFromString(dto.dateOfBirth())));
            System.out.println(encryptedHealthDataId);
            return new Patient(
                    utilService.generateId(),
                    encryptedFirstName,
                    encryptedLastName,
                    encryptedDateOfBirth,
                    encryptedGender,
                    encryptedEmergencyContact,
                    encryptedNationality,
                    encryptedMaritalStatus,
                    encryptedPrimaryLanguage,
                    encryptedOccupation,
                    encryptedInsuranceNr,
                    contactInfo,
                    encryptedHealthDataId
            );
        } else {
            // Updating an existing patient
            String encryptedHealthDataId = existingPatient.healthDataId();

            return existingPatient
                    .withFirstname(encryptedFirstName)
                    .withLastname(encryptedLastName)
                    .withDateOfBirth(encryptedDateOfBirth)
                    .withGender(encryptedGender)
                    .withEmergencyContact(encryptedEmergencyContact)
                    .withNationality(encryptedNationality)
                    .withMaritalStatus(encryptedMaritalStatus)
                    .withPrimaryLanguage(encryptedPrimaryLanguage)
                    .withOccupation(encryptedOccupation)
                    .withInsuranceNr(encryptedInsuranceNr)
                    .withContactInformation(contactInfo)
                    .withHealthDataId(encryptedHealthDataId);
        }
    }

    protected String createEmptyHealthDataObject(String gender, int ageAtFirstAdmission) {
        LocalDate firstAdmissionDate = LocalDate.now();
        HealthDataDto newHealthData = new HealthDataDto(gender, ageAtFirstAdmission, firstAdmissionDate,
                new ArrayList<>());
        HealthData result = healthDataService.createHealthData(newHealthData);
        return result.id();
    }

    protected int calculateAgeFromString(String birthDateString) {
        LocalDate birthDate = parseDateString(birthDateString);
        if (birthDate != null) {
            LocalDate currentDate = LocalDate.now();
            if (birthDate.isBefore(currentDate)) {
                return Period.between(birthDate, currentDate).getYears();
            } else {
                throw new IllegalArgumentException("Date of birth may not be in the future.");
            }
        } else {
            throw new IllegalArgumentException("Invalid date format.");
        }
    }

    protected LocalDate parseDateString(String dateString) {
        try {
            if (dateString.contains("-")) {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else if (dateString.contains(".")) {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                throw new IllegalArgumentException("Invalid date format.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format.", e);
        }
    }

    protected ContactInformation createEncryptedContactInformation(ContactInformation contact) {
        return new ContactInformation(
                !contact.phoneNr().isEmpty() ? encryptionService.encrypt(contact.phoneNr()) : "",
                !contact.email().isEmpty() ? encryptionService.encrypt(contact.email()) : "",
                encryptionService.encrypt(contact.address()),
                encryptionService.encrypt(contact.town())
        );
    }
}
