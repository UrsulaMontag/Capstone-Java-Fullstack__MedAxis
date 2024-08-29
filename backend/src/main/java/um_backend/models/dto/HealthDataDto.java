package um_backend.models.dto;

import lombok.With;
import um_backend.models.MedicalExamination;

import java.time.LocalDate;
import java.util.List;

@With
public record HealthDataDto(
        String gender,
        int ageAtFirstAdmission,
        LocalDate firstAdmissionDate,
        List<MedicalExamination> medicalExaminations
) {
}
