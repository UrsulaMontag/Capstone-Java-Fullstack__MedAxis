package um_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import um_backend.clients.IcdApiClient;

@Service
public class IcdApiService {
    private final IcdApiClient icdApiClient;

    @Autowired
    public IcdApiService(IcdApiClient icdApiClient) {
        this.icdApiClient = icdApiClient;
    }

    public String getIcdData(String uri) {
        return icdApiClient.getURI(uri);
    }

    public String getIcdData() {
        try {
            return icdApiClient.getIcdDetails();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get ICD details", e);
        }
    }
}
