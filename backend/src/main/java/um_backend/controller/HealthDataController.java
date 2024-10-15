package um_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.models.MedicalExamination;
import um_backend.models.dto.HealthDataDto;
import um_backend.services.HealthDataService;

@RestController
@RequestMapping("/api/health_data")
@RequiredArgsConstructor
public class HealthDataController {
    private final HealthDataService healthDataService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{dataId}/add-examination")
    public HealthData addExamination(@PathVariable String dataId, @RequestBody MedicalExamination newExamination)
            throws InvalidIdException {
        return healthDataService.addExaminationWithIcdCodes(dataId, newExamination);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{dataId}")
    public HealthData getHealthDataByPatientId(@PathVariable String dataId) throws InvalidIdException {
        return healthDataService.getHealthDataById(dataId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public HealthData createHealthData(@RequestBody HealthDataDto healthDataDto) throws IllegalArgumentException {
        return healthDataService.createHealthData(healthDataDto);
    }

}
