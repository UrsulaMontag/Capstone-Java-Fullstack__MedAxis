package um_backend.clients;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class IcdApiClient {
    private final RestClient restClient;

    @Value("${icd.token.endpoint}")
    private String TOKEN_ENDPOINT;
    @Value("${icd.client.id}")
    private String CLIENT_ID;
    @Value("${icd.client.secret}")
    private String CLIENT_SECRET;

    public IcdApiClient() {
        restClient = RestClient.builder().build();
    }

    public String getToken() {
        String SCOPE = "icdapi_access";
        String GRANT_TYPE = "client_credentials";

        String response = restClient.post()
                .uri(TOKEN_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&scope=" + SCOPE + "&grant_type=" + GRANT_TYPE)
                .retrieve()
                .body(String.class);

        JSONObject jsonObj = new JSONObject(response);
        return jsonObj.getString("access_token");
    }

    public String getURI(String token, String uri) throws RestClientException {
        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .header("API-Version", "v2")
                .retrieve()
                .body(String.class);
    }

}
