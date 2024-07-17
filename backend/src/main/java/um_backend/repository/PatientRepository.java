package um_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import um_backend.models.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
}
