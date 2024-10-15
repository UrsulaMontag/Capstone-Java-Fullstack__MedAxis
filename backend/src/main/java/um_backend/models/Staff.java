package um_backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document("staff")
public record Staff(
        @Id
        String id,
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
