package um_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import um_backend.models.HealthData;

public interface HealthDataRepository extends MongoRepository<HealthData, String> {
    HealthData findByPatientId(String patientId);
}
