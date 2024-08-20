package um_backend.clients;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import um_backend.exeptions.BadRequestException;
import um_backend.exeptions.NotFoundException;

@Component
public class IcdApiClient {
    private final RestClient restClient;
    @Value("${icd.token.endpoint}")
    private String tokenEndpoint;
    @Value("${icd.client.id}")
    private String clientId;
    @Value("${icd.client.secret}")
    private String clientSecret;
    @Value("${icd.api.base.uri}")
    private String baseUri;

    public IcdApiClient() {
        restClient = RestClient.create(tokenEndpoint);

    }

    public String getToken() {
        String scope = "icdapi_access";
        String grantType = "client_credentials";
        try {
            String response = restClient.post()
                    .uri(tokenEndpoint)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("client_id=" + clientId + "&client_secret=" + clientSecret + "&scope=" + scope + "&grant_type=" + grantType)
                    .retrieve()
                    .body(String.class);
            JSONObject jsonObj = new JSONObject(response);
            return jsonObj.getString("access_token");
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("Failed to get access token: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Failed to retrieve token: " + e.getMessage(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get access token:" + e.getMessage());
        }
    }


    public String getURI(String uri) throws IllegalArgumentException {
        try {
            String token = getToken();
            return restClient.get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                    .header("API-Version", "v2")
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to get URI: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Failed to retrieve URI: " + e.getMessage());
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to access URI" + e.getMessage());
        }
    }

    public String getIcdDetails() {
        return getURI(baseUri + "/entity");
    }

    public String searchIcd(String query, boolean subtreeFilterUsesFoundationDescendants,
                            boolean includeKeywordResult,
                            boolean useFlexisearch, boolean flatResults,
                            boolean highlightingEnabled, boolean medicalCodingMode) {
        String uri = UriComponentsBuilder.fromHttpUrl(baseUri + "/release/11/2024-01/mms/search")
                .queryParam("q", query)
                .queryParam("subtreeFilterUsesFoundationDescendants", subtreeFilterUsesFoundationDescendants)
                .queryParam("includeKeywordResult", includeKeywordResult)
                .queryParam("useFlexisearch", useFlexisearch)
                .queryParam("flatResults", flatResults)
                .queryParam("highlightingEnabled", highlightingEnabled)
                .queryParam("medicalCodingMode", medicalCodingMode)
                .toUriString();
        try {

            return restClient.post()
                    .uri(uri)
                    .header("Authorization", "Bearer " + getToken())
                    .header("Accept", "application/json")
                    .header("API-Version", "v2")
                    .header("Accept-Language", "en")
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("Failed to search ICD: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Failed to get search ICD: " + e.getMessage());
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to search ICD" + e.getMessage());
        }
    }


}
