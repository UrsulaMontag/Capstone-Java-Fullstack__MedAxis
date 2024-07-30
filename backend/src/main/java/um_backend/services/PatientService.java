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
        if (patient.contact() != null) {
            if (!dataValidationService.isValidPhoneNumber(patient.contact().phoneNr())) {
                throw new IllegalArgumentException("Invalid phone number format.");
            }
            if (!dataValidationService.isValidEmail(patient.contact().email())) {
                throw new IllegalArgumentException("Invalid email address format.");
            }
        }
    }

    protected Patient decryptPatient(Patient patient) {
        return patient.withFirstname(encryptionService.decrypt(patient.firstname()))
                .withLastname(encryptionService.decrypt(patient.lastname()))
                .withDateOfBirth(encryptionService.decryptDate(patient.dateOfBirth()))
                .withInsuranceNr(encryptionService.decrypt(patient.insuranceNr()))
                .withContactInformation(patient.contactInformation() != null ?
                        new ContactInformation(
                                encryptionService.decrypt(patient.contactInformation().phoneNr()),
                                encryptionService.decrypt(patient.contactInformation().email()),
                                encryptionService.decrypt(patient.contactInformation().address()),
                                encryptionService.decrypt(patient.contactInformation().town())
                        )
                        : null);
    }

    protected Patient createOrUpdatePatient(PatientPersonalDTO dto, Patient existingPatient) {
        String encryptedFirstName = encryptionService.encrypt(dto.firstname());
        String encryptedLastName = encryptionService.encrypt(dto.lastname());
        String encryptedDateOfBirth = encryptionService.encryptDate(dto.dateOfBirth());
        String encryptedInsuranceNr = encryptionService.encrypt(dto.insuranceNr());
        ContactInformation contactInfo = createEncryptedContactInformation(dto.contact());

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
        if (contact == null) {
            return null;
        }

        return new ContactInformation(
                contact.phoneNr() != null ? encryptionService.encrypt(contact.phoneNr()) : null,
                contact.email() != null ? encryptionService.encrypt(contact.email()) : null,
                encryptionService.encrypt(contact.address()),
                encryptionService.encrypt(contact.town())
        );
    }
}
