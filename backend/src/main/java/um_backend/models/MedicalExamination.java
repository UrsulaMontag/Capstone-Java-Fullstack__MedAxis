package um_backend.models;

import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
public record MedicalExamination(
        LocalDateTime examinationDate,
        List<IcdCode> icdCodes,
        String symptoms,
        String diagnosis,
        List<String> medications,
        List<Treatment> treatments,
        List<VitalSigns> vitalSigns,
        String additionalNotes
) {
}
