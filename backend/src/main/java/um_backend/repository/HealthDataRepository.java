package um_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import um_backend.models.HealthData;

@Repository
public interface HealthDataRepository extends MongoRepository<HealthData, String> {
}
