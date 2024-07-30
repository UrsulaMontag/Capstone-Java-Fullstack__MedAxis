package um_backend.models.dto;

import lombok.With;
import um_backend.models.ContactInformation;

@With
public record PatientPersonalDTO(
        String firstname,
        String lastname,
        String dateOfBirth,
        String insuranceNr,
        ContactInformation contact
) {
}