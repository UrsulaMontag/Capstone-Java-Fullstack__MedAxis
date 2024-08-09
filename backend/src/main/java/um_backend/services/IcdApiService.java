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
        String token = icdApiClient.getToken();
        return icdApiClient.getURI(token, uri);
    }
}
