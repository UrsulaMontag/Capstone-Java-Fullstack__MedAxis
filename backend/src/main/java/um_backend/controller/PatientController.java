package um_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPostDto;
import um_backend.services.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/patients")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/patients/{id}")
    public Patient getPatientById(@PathVariable String id) throws InvalidIdException {
        return patientService.getPatientById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/add")
    public Patient cteatePatient(@RequestBody PatientPostDto newPatientDto) {
        return patientService.createPatient(newPatientDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/patients/edit/{id}")
    public Patient updatePatient(@PathVariable String id, @RequestBody PatientPostDto updatedPatientDto) throws InvalidIdException {
        return patientService.updatePatientById(id, updatedPatientDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/patients/{id}")
    public void deletePatient(@PathVariable String id) throws InvalidIdException {
        patientService.deletePatientById(id);
    }
}
