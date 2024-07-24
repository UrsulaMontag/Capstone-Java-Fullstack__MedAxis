package um_backend.models.dto;

import lombok.With;

import java.time.LocalDate;

@With
public record PatientPersonalDTO(
        String firstname,
        String lastname,
        LocalDate dateOfBirth
) {
}