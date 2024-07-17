package um_backend.models.dto;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@With
@Document("patients")
public record PatientPostDto(
        String firstname,
        String lastname,
        LocalDate dateOfBirth
) {
}