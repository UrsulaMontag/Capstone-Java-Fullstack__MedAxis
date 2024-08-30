package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.models.IcdCode;
import um_backend.models.dto.HealthDataDto;
import um_backend.repository.HealthDataRepository;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataRepository healthDataRepository;
    private final UtilService utilService;

    public HealthData addIcdCodeToHealthData(String dataId, IcdCode icdCode) throws InvalidIdException {
        try {
            HealthData healthData = healthDataRepository.findById(dataId)
                    .orElseThrow(() -> new InvalidIdException("Patients data are not registered"));
            if (healthData.medicalExaminations().isEmpty()) {
                throw new IllegalArgumentException("No medical examinations found to add ICD code.");
            }
            healthData.medicalExaminations().getLast().icdCodes().add(icdCode);
            return healthDataRepository.save(healthData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Health data contains problems");
        }
    }

    public HealthData updateHealthData(String dataId, HealthDataDto healthDataDto) {
        try {
            HealthData healthData = healthDataRepository.findById(dataId)
                    .orElseGet(() -> new HealthData(utilService.generateId(), healthDataDto.gender(), healthDataDto.ageAtFirstAdmission(),
                            healthDataDto.firstAdmissionDate(), healthDataDto.medicalExaminations())
                            .withGender(healthDataDto.gender())
                            .withAgeAtFirstAdmission(healthDataDto.ageAtFirstAdmission())
                            .withFirstAdmissionDate(healthDataDto.firstAdmissionDate())
                            .withMedicalExaminations(healthDataDto.medicalExaminations()));

            return healthDataRepository.save(healthData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Health data contains problems");
        }
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
                healthDataDto.ageAtFirstAdmission(), healthDataDto.firstAdmissionDate(), healthDataDto.medicalExaminations());
        return healthDataRepository.save(healthData);
    }
}
