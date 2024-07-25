package um_backend.models.dto;

import lombok.With;
import um_backend.models.ContactInformation;

import java.time.LocalDate;

@With
public record PatientPersonalDTO(
        String firstname,
        String lastname,
        LocalDate dateOfBirth,
        String insuranceNr,
        ContactInformation contact
) {
}