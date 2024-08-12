package um_backend.clients;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;

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

    public String getURI(String uri) throws RestClientException {
        String token = getToken();
        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .header("API-Version", "v2")
                .retrieve()
                .body(String.class);
    }

    public String getIcdDetails() throws Exception {
        String token = getToken();
        String icdEntityUri = "https://id.who.int/icd/entity";
        return restClient
                .get()
                .uri(new URI(icdEntityUri))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .header("API-Version", "v2")
                .retrieve()
                .body(String.class);
    }

}
