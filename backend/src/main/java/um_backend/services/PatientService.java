package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPostDto;
import um_backend.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UtilService utilService;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(String id) throws InvalidIdException {
        return patientRepository.findById(id).orElseThrow(() -> new InvalidIdException("Patient with id " + id + "not found!"));
    }

    public Patient createPatient(PatientPostDto patient) {
        Patient newPatient = new Patient(utilService.generateId(), patient.firstname(), patient.lastname(), patient.dateOfBirth());
        return patientRepository.save(newPatient);
    }
}
