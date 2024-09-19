package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.models.MedicalExamination;
import um_backend.models.dto.HealthDataDto;
import um_backend.repository.HealthDataRepository;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataRepository healthDataRepository;
    private final UtilService utilService;

    public HealthData addExaminationWithIcdCodes(String dataId, MedicalExamination newExamination)
            throws InvalidIdException {
        HealthData healthData = healthDataRepository.findById(dataId)
                .orElseThrow(() -> new InvalidIdException("Patients data are not registered"));

        List<MedicalExamination> updatedExaminations = new ArrayList<>(healthData.medicalExaminations());
        updatedExaminations.add(newExamination);

        HealthData updatedHealthData = healthData.withMedicalExaminations(updatedExaminations);
        return healthDataRepository.save(updatedHealthData);
    }

    public HealthData getHealthDataById(String id) throws InvalidIdException {
        return healthDataRepository.findById(id).orElseThrow(
                () -> new InvalidIdException("Data with id " + id + "not found!"));
    }

    public HealthData createHealthData(HealthDataDto healthDataDto) {
        if (healthDataDto == null) {
            throw new IllegalArgumentException("HealthData cannot be null");
        }
        HealthData healthData = new HealthData(utilService.generateId(), healthDataDto.gender(),
                healthDataDto.ageAtFirstAdmission(), healthDataDto.firstAdmissionDate(),
                healthDataDto.medicalExaminations());
        return healthDataRepository.save(healthData);
    }

    public HealthData updateHealthData(String dataId, MedicalExamination newExamination) throws InvalidIdException {
        HealthData healthData = healthDataRepository.findById(dataId)
                .orElseThrow(() -> new InvalidIdException("Patient data contains problems. No health data object."));

        List<MedicalExamination> updatedExaminations = new ArrayList<>(healthData.medicalExaminations());
        updatedExaminations.add(newExamination);

        HealthData updatedHealthData = healthData.withMedicalExaminations(updatedExaminations);
        return healthDataRepository.save(updatedHealthData);
    }
}
