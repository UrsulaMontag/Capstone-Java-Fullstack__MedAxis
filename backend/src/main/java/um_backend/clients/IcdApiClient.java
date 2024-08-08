package um_backend.clients;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
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

    // get the OAUTH2 token
    public String getToken() throws Exception {

        System.out.println("Getting token...");

        // set parameters to post
        String SCOPE = "icdapi_access";
        String GRANT_TYPE = "client_credentials";
        String urlParameters =
                "client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) +
                        "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8) +
                        "&scope=" + URLEncoder.encode(SCOPE, StandardCharsets.UTF_8) +
                        "&grant_type=" + URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //Perform post request
        String response = restClient
                .post()
                .uri(new URI(TOKEN_ENDPOINT))
                .headers(h -> h.addAll(headers))
                .body(urlParameters)
                .retrieve()
                .body(String.class);

        //Parse JSON response
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("access_token");
    }


    // access ICD API
    public String getIcdDetails() throws Exception {
        System.out.println("Getting URI...");

        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Accept-Language", "en");
        headers.set("Api-Version", "v2");

        String icdEntityUri = "https://id.who.int/icd/entity";
        //Perform get request
        return restClient
                .get()
                .uri(new URI(icdEntityUri))
                .headers(h -> h.addAll(headers))
                .retrieve()
                .body(String.class);
    }
}
