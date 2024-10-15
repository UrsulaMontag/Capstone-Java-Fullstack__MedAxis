package um_backend.models;

public record VitalSigns(
        double temperature,
        int heartRate,
        int bloodPressureSystolic,
        int bloodPressureDiastolic,
        int respiratoryRate) {
}
