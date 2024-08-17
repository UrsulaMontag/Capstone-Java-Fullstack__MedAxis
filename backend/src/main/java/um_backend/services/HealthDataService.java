package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.repository.HealthDataRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataRepository healthDataRepository;
    private final UtilService utilService;

    public HealthData addOrUpdateIcdCodes(String dataId, String icdCode) {
        try {
            HealthData healthData = healthDataRepository.findById(dataId)
                    .orElseGet(() -> new HealthData(utilService.generateId(), new ArrayList<>()));
            healthData.icdCodes().add(icdCode);
            return healthDataRepository.save(healthData);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Health data contains problems");
        }

    }

    public HealthData getHealthDataById(String id) throws InvalidIdException {
        return healthDataRepository.findById(id).orElseThrow(
                () -> new InvalidIdException("Data with id " + id + "not found!"));
    }

    public HealthData createHealthData(HealthData healthData) {
        if (healthData == null) {
            throw new IllegalArgumentException("HealthData cannot be null");
        }
        return healthDataRepository.save(healthData);
    }
}
