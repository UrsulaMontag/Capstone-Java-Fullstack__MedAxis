package um_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import um_backend.clients.IcdApiClient;

@Service
@RequiredArgsConstructor
public class IcdApiService {
    private final IcdApiClient icdApiClient;

    public String getIcdData() {
        try {
            return icdApiClient.getIcdDetails();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ICD details", e);
        }
    }
}
