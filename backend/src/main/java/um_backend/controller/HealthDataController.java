package um_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.models.HealthData;
import um_backend.services.HealthDataService;

@RestController
@RequestMapping("/api/health_data")
@RequiredArgsConstructor
public class HealthDataController {
    private final HealthDataService healthDataService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{patientId}/add-icd-code")
    public HealthData addIcdCodeToPatient(@PathVariable String patientId, @RequestBody String icdCode) {
        return healthDataService.addOrUpdateHealthData(patientId, icdCode);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{patientId}")
    public HealthData getHealthDataByPatientId(@PathVariable String patientId) {
        return healthDataService.getHealthDataByPatientId(patientId);
    }
}
