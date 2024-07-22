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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable String id) throws InvalidIdException {
        return patientService.getPatientById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Patient cteatePatient(@RequestBody PatientPostDto newPatientDto) {
        return patientService.createPatient(newPatientDto);
    }
}
