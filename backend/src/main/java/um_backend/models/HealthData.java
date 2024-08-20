package um_backend.models;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document("health_data")
public record HealthData(
        @Id
        String id,
        List<String> icdCodes
) {
}
