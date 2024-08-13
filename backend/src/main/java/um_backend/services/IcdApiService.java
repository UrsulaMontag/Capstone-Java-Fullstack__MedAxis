package um_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import um_backend.clients.IcdApiClient;

import java.util.HashMap;
import java.util.Map;

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

    public String getToken() {
        try {
            String token = icdApiClient.getToken();
            Map<String, String> response = new HashMap<>();
            response.put("access_token", token);
            return token;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        }
    }

    public String searchIcd(String query) {
        return icdApiClient.searchIcd(query);
    }
}
