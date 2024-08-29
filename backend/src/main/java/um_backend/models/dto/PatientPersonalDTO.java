package um_backend.models.dto;

import lombok.With;
import um_backend.models.ContactInformation;
import um_backend.models.EmergencyContact;

@With
public record PatientPersonalDTO(
        String firstname,
        String lastname,
        String dateOfBirth,
        String gender,
        EmergencyContact emergencyContact,
        String nationality,
        String maritalStatus,
        String primaryLanguage,
        String occupation,
        String insuranceNr,
        ContactInformation contactInformation
) {
}