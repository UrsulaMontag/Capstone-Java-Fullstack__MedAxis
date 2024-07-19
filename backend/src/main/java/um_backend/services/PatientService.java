package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPostDto;
import um_backend.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UtilService utilService;

    public Patient createPatient(PatientPostDto patient) {
        Patient newPatient = new Patient(utilService.generateId(), patient.firstname(), patient.lastname(), patient.dateOfBirth());
        return patientRepository.save(newPatient);
    }
}
