package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.models.HealthData;
import um_backend.repository.HealthDataRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataRepository healthDataRepository;
    private final UtilService utilService;

    public HealthData addOrUpdateHealthData(String patientId, String icdCode) {
        HealthData healthData = healthDataRepository.findByPatientId(patientId);
        if (healthData == null) {
            healthData = new HealthData(utilService.generateId(), patientId, List.of(icdCode));
        } else {
            healthData.icdCodes().add(icdCode);
        }
        return healthDataRepository.save(healthData);
    }

    public HealthData getHealthDataByPatientId(String patientId) {
        return healthDataRepository.findByPatientId(patientId);
    }
}
