package um_backend.models.dto;

import lombok.With;

import java.util.List;

@With
public record HealthDataDto(List<String> icdCodes) {
}
