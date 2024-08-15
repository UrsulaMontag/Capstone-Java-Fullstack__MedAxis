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
        return icdApiClient.getIcdDetails();

    }

    public String getToken() {
        return icdApiClient.getToken();
    }

    public String searchIcd(String query, boolean subtreeFilterUsesFoundationDescendants,
                            boolean includeKeywordResult, boolean useFlexisearch, boolean flatResults,
                            boolean highlightingEnabled, boolean medicalCodingMode) {
        return icdApiClient.searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult, useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);
    }
}
