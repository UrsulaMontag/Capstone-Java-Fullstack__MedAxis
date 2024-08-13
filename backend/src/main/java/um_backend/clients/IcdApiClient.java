package um_backend.clients;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public String searchIcd(String query) {
        String baseUri = "https://id.who.int/icd/entity/search?q=";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String icdSearchUri = baseUri + encodedQuery;

        // Sende die Anfrage und erhalte die Antwort als String
        String response = getURI(icdSearchUri);

        // Convert response to JSON object
        JSONObject jsonResponse = new JSONObject(response); // Hier wird die API-Antwort geparsed

        // Create a new JSON object for the formatted response
        JSONObject formattedResponse = new JSONObject();
        JSONObject searchResult = new JSONObject();

        // Extract "words" and "destinationEntities"
        JSONArray words = jsonResponse.optJSONArray("words");  // Could be null
        JSONArray destinationEntities = jsonResponse.optJSONArray("destinationEntities");  // Empty array if none

        // If "words" is null, create an empty array
        if (words == null) {
            words = new JSONArray();
        }

        // If "destinationEntities" is not empty, add them to "words"
        if (destinationEntities != null) {
            for (int i = 0; i < destinationEntities.length(); i++) {
                JSONObject entity = destinationEntities.getJSONObject(i);
                JSONObject word = new JSONObject();
                word.put("term", entity.optString("label", "Unknown Label"));  // Label or fallback
                word.put("code", entity.optString("code", "Unknown Code"));    // Code or fallback
                words.put(word);
            }
        }

        // Put "words" into the searchResult
        searchResult.put("words", words);

        // Add searchResult to the formatted response
        formattedResponse.put("searchResult", searchResult);

        // Return the formatted response as a string
        return formattedResponse.toString();
    }
}
