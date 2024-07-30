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

    private void validatePatientData(PatientPersonalDTO patient) {
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


    public List<Patient> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patient -> patient.withFirstname(encryptionService.decrypt(patient.firstname()))
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
                                : null))
                .toList();
    }

    public Patient getPatientById(String id) throws InvalidIdException {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
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

    public Patient createPatient(PatientPersonalDTO patient) throws IllegalArgumentException {
        if (patient == null) {
            throw new IllegalArgumentException("PatientPersonalDTO cannot be null");
        }

        if (utilService == null || encryptionService == null || patientRepository == null) {
            throw new IllegalStateException("Required services are not initialized");
        }
        validatePatientData(patient);
        String encryptedFirstName = encryptionService.encrypt(patient.firstname());
        String encryptedLastName = encryptionService.encrypt(patient.lastname());
        String encryptedDateOfBirth = encryptionService.encryptDate(patient.dateOfBirth());
        String encryptedInsuranceNr = encryptionService.encrypt(patient.insuranceNr());

        ContactInformation contactInfo = null;
        if (patient.contact() != null) {
            contactInfo = new ContactInformation(
                    patient.contact().phoneNr() != null ? encryptionService.encrypt(patient.contact().phoneNr()) : null,
                    patient.contact().email() != null ? encryptionService.encrypt(patient.contact().email()) : null,
                    encryptionService.encrypt(patient.contact().address()),
                    encryptionService.encrypt(patient.contact().town())
            );
        }

        Patient newPatient = new Patient(
                utilService.generateId(),
                encryptedFirstName,
                encryptedLastName,
                encryptedDateOfBirth,
                encryptedInsuranceNr,
                contactInfo
        );

        return patientRepository.save(newPatient);
    }

    public Patient updatePatientById(String id, PatientPersonalDTO patient) throws InvalidIdException {
        validatePatientData(patient);
        Patient currentPatient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
        String encryptedFirstname = encryptionService.encrypt(patient.firstname());
        String encryptedLastname = encryptionService.encrypt(patient.lastname());
        String encryptedDateOfBirth = encryptionService.encryptDate(patient.dateOfBirth());
        String encryptedInsuranceNr = encryptionService.encrypt(patient.insuranceNr());

        ContactInformation encryptedContactInfo = null;
        if (patient.contact() != null) {
            encryptedContactInfo = new ContactInformation(
                    encryptionService.encrypt(patient.contact().phoneNr()),
                    encryptionService.encrypt(patient.contact().email()),
                    encryptionService.encrypt(patient.contact().address()),
                    encryptionService.encrypt(patient.contact().town())
            );
        }

        Patient updatedPatient = currentPatient
                .withFirstname(encryptedFirstname)
                .withLastname(encryptedLastname)
                .withDateOfBirth(encryptedDateOfBirth)
                .withInsuranceNr(encryptedInsuranceNr)
                .withContactInformation(encryptedContactInfo);

        return patientRepository.save(updatedPatient);
    }

    public void deletePatientById(String id) throws InvalidIdException {
        if (patientRepository.existsById(id)) patientRepository.deleteById(id);
        else throw new InvalidIdException("Patient with id " + id + " not found!");
    }
}
