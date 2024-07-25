package um_backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@With
@Document("patients")
public record Patient(
        @Id
        String id,
        String firstname,
        String lastname,
        LocalDate dateOfBirth,
        String insuranceNr,
        ContactInformation contactInformation
) {
}
