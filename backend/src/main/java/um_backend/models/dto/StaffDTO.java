package um_backend.models.dto;

import um_backend.models.ContactInformation;

import java.util.List;

public record StaffDTO(
        String firstname,
        String lastname,
        String dateOfBirth,
        String gender,
        ContactInformation contactInformation,
        String role,
        String specialty,
        List<String> wards
) {
}
