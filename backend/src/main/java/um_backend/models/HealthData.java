package um_backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@With
@Document("health_data")
public record HealthData(
        @Id
        String id,
        String gender,
        int ageAtFirstAdmission,
        LocalDate firstAdmissionDate,
        List<MedicalExamination> medicalExaminations
) {
}
