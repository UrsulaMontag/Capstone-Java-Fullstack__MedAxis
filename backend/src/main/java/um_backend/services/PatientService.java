package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.ContactInformation;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPersonalDTO;
import um_backend.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UtilService utilService;
    private final DataValidationService dataValidationService;
    private final EncryptionService encryptionService;

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
        return patientRepository.save(newPatient);
    }

    public Patient updatePatientById(String id, PatientPersonalDTO patient) throws InvalidIdException {
        validatePatientData(patient);
        Patient currentPatient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
        Patient updatedPatient = createOrUpdatePatient(patient, currentPatient);
        return patientRepository.save(updatedPatient);
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
    }

    protected Patient decryptPatient(Patient patient) {
        String decryptedFirstname = encryptionService.decrypt(patient.firstname());
        String decryptedLastname = encryptionService.decrypt(patient.lastname());
        String decryptedDateOfBirth = encryptionService.decrypt(patient.dateOfBirth());
        String decryptedInsuranceNr = encryptionService.decrypt(patient.insuranceNr());

        ContactInformation decryptedContactInformation = new ContactInformation(
                (!patient.contactInformation().phoneNr().isEmpty() ?
                        encryptionService.decrypt(patient.contactInformation().phoneNr()) : ""),
                (!patient.contactInformation().email().isEmpty() ?
                        encryptionService.decrypt(patient.contactInformation().email()) : ""),
                encryptionService.decrypt(patient.contactInformation().address()),
                encryptionService.decrypt(patient.contactInformation().town())
        );
        return new Patient(patient.id(), decryptedFirstname, decryptedLastname,
                decryptedDateOfBirth, decryptedInsuranceNr, decryptedContactInformation);
    }

    protected Patient createOrUpdatePatient(PatientPersonalDTO dto, Patient existingPatient) {
        String encryptedFirstName = encryptionService.encrypt(dto.firstname());
        String encryptedLastName = encryptionService.encrypt(dto.lastname());
        String encryptedDateOfBirth = encryptionService.encrypt(dto.dateOfBirth());
        String encryptedInsuranceNr = encryptionService.encrypt(dto.insuranceNr());
        ContactInformation contactInfo = createEncryptedContactInformation(dto.contactInformation());

        if (existingPatient == null) {
            // Creating a new patient
            return new Patient(
                    utilService.generateId(),
                    encryptedFirstName,
                    encryptedLastName,
                    encryptedDateOfBirth,
                    encryptedInsuranceNr,
                    contactInfo
            );
        } else {
            // Updating an existing patient
            return existingPatient
                    .withFirstname(encryptedFirstName)
                    .withLastname(encryptedLastName)
                    .withDateOfBirth(encryptedDateOfBirth)
                    .withInsuranceNr(encryptedInsuranceNr)
                    .withContactInformation(contactInfo);
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
