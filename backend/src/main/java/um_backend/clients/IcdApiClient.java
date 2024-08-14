package um_backend.clients;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class IcdApiClient {
    private final RestClient restClient;
    @Value("${icd.token.endpoint}")
    private String tokenEndpoint;
    @Value("${icd.client.id}")
    private String clientId;
    @Value("${icd.client.secret}")
    private String clientSecret;

    public IcdApiClient() {
        restClient = RestClient.builder().build();
    }

    public String getToken() {
        String scope = "icdapi_access";
        String grantType = "client_credentials";
        String response = restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=" + clientId + "&client_secret=" + clientSecret + "&scope=" + scope + "&grant_type=" + grantType)
                .retrieve()
                .body(String.class);
        JSONObject jsonObj = new JSONObject(response);
        return jsonObj.getString("access_token");
    }


    public String getURI(String uri) throws IllegalArgumentException {
        String token = getToken();
        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .header("API-Version", "v2")
                .retrieve()
                .body(String.class);
    }

    public String getIcdDetails() throws Exception {
        String icdEntityUri = "https://id.who.int/icd/entity";
        return getURI(icdEntityUri);
    }

    public String searchIcd(String query, boolean subtreeFilterUsesFoundationDescendants,
                            boolean includeKeywordResult,
                            boolean useFlexisearch, boolean flatResults,
                            boolean highlightingEnabled, boolean medicalCodingMode) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://id.who.int/icd/release/11/2024-01/mms/search")
                .queryParam("q", query)
                .queryParam("subtreeFilterUsesFoundationDescendants", subtreeFilterUsesFoundationDescendants)
                .queryParam("includeKeywordResult", includeKeywordResult)
                .queryParam("useFlexisearch", useFlexisearch)
                .queryParam("flatResults", flatResults)
                .queryParam("highlightingEnabled", highlightingEnabled)
                .queryParam("medicalCodingMode", medicalCodingMode)
                .toUriString();

        return restClient.post()
                .uri(uri)
                .header("Authorization", "Bearer " + getToken())
                .header("Accept", "application/json")
                .header("API-Version", "v2")
                .header("Accept-Language", "en")
                .retrieve()
                .body(String.class);
    }


}
