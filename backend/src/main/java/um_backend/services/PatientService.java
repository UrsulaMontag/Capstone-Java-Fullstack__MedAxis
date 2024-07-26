package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
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
        return patientRepository.findAll();
    }

    public Patient getPatientById(String id) throws InvalidIdException {
        return patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
    }

    public Patient createPatient(PatientPersonalDTO patient) throws IllegalArgumentException {
        validatePatientData(patient);
        Patient newPatient = new Patient(
                utilService.generateId(),
                patient.firstname(),
                patient.lastname(),
                patient.dateOfBirth(),
                patient.insuranceNr(),
                patient.contact());
        return patientRepository.save(newPatient);
    }

    public Patient updatePatientById(String id, PatientPersonalDTO patient) throws InvalidIdException {
        validatePatientData(patient);
        Patient currentPatient = patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
        Patient updatedPatient = currentPatient
                .withFirstname(patient.firstname())
                .withLastname(patient.lastname())
                .withDateOfBirth(patient.dateOfBirth());
        return patientRepository.save(updatedPatient);
    }

    public void deletePatientById(String id) throws InvalidIdException {
        if (patientRepository.existsById(id)) patientRepository.deleteById(id);
        else throw new InvalidIdException("Patient with id " + id + " not found!");
    }
}
