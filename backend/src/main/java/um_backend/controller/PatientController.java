package um_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPostDto;
import um_backend.services.PatientService;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Patient cteatePatient(@RequestBody PatientPostDto newPatient) {
        return patientService.createPatient(newPatient);
    }
}
