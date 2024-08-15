package um_backend.clients;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class IcdApiClientTest {

    private static MockWebServer mockWebServer;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("icd.client.id", () -> "mockClientId");
        registry.add("icd.client.secret", () -> "mockClientSecret");
        registry.add("icd.token.endpoint", () -> mockWebServer.url("/token").toString());
        registry.add("icd.api.base.uri", () -> mockWebServer.url("/api").toString());
        registry.add("ENCRYPTION_PASSWORD", () -> "password");
        registry.add("ENCRYPTION_SALT", () -> "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetToken_Success() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/mms/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("client_id=mockClientId&client_secret=mockClientSecret&scope=icdapi_access&grant_type=client_credentials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("mockToken123"));
    }

    @Test
    void testGetToken_InvalidClient() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400) // Bad Request
                .setBody("{\"error\":\"invalid_client\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/mms/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("client_id=mockClientId&client_secret=mockClientSecret&scope=icdapi_access&grant_type=client_credentials"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting 400 Bad Request
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to get access token: 400 Bad Request: \"{\"error\":\"invalid_client\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/mms/token"));
    }

    @Test
    void testGetToken_ServerError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500) // Internal Server Error
                .setBody("{\"error\":\"server_error\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/mms/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("client_id=mockClientId&client_secret=mockClientSecret&scope=icdapi_access&grant_type=client_credentials"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()) // Expecting 500 Internal Server Error
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to retrieve token: 500 Internal Server Error: \"{\"error\":\"server_error\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/mms/token"));
    }

    @Test
    void testGetURI_Success() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";
        String mockUriResponse = """
                {
                    "@context": "http://id.who.int/icd/contexts/contextForTopLevel.json",
                    "@id": "http://id.who.int/icd/entity",
                    "title": {
                        "@language": "en",
                        "@value": "WHO Family of International Classifications Foundation"
                    },
                    "releaseId": "2024-01",
                    "availableLanguages": ["ar", "cs", "en", "es", "fr", "pt", "ru", "tr", "uz", "zh"],
                    "releaseDate": "2024-01-21",
                    "child": [
                        "http://id.who.int/icd/entity/448895267",
                        "http://id.who.int/icd/entity/1405434703"
                    ],
                    "browserUrl": "https://icd.who.int/browse/2024-01/foundation/en"
                }
                """;

        // mock token response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        // mock URI response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockUriResponse)
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/v2/mms")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mockUriResponse));
    }

    @Test
    void testGetURI_NotFound() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        // Mock token response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        // Mock URI not found response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404) // Not Found
                .setBody("{\"error\":\"not_found\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/v2/mms")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()) // Expecting 404 Not Found
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to get URI: 404 Not Found: \"{\"error\":\"not_found\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/v2/mms"));
    }

    @Test
    void testGetURI_ServerError() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        // Mock token response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        // Mock URI server error response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500) // Internal Server Error
                .setBody("{\"error\":\"server_error\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/icd/entity/icd/release/11/v2/mms")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()) // Expecting 500 Internal Server Error
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to retrieve URI: 500 Internal Server Error: \"{\"error\":\"server_error\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/v2/mms"));
    }


    @Test
    void testSearchIcd_Success() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";
        String mockSearchResponse = """
                    {
                        "error": false,
                        "errorMessage": null,
                        "resultChopped": false,
                        "wordSuggestionsChopped": false,
                        "guessType": 2,
                        "uniqueSearchId": "1ac9329b-e685-492a-acc9-d294bc18eec8",
                        "words": [],
                        "destinationEntities": []
                    }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockSearchResponse)
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/icd/entity/icd/release/11/v2/mms/search")
                        .param("q", "mockQuery")
                        .param("subtreeFilterUsesFoundationDescendants", "true")
                        .param("includeKeywordResult", "true")
                        .param("useFlexisearch", "true")
                        .param("flatResults", "true")
                        .param("highlightingEnabled", "true")
                        .param("medicalCodingMode", "true")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mockSearchResponse));
    }

    @Test
    void testSearchIcd_BadRequest() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        // Mock token response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        // Mock search bad request response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400) // Bad Request
                .setBody("{\"error\":\"invalid_request\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/icd/entity/icd/release/11/v2/mms/search")
                        .param("q", "mockQuery")
                        .param("subtreeFilterUsesFoundationDescendants", "true")
                        .param("includeKeywordResult", "true")
                        .param("useFlexisearch", "true")
                        .param("flatResults", "true")
                        .param("highlightingEnabled", "true")
                        .param("medicalCodingMode", "true")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting 400 Bad Request
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to search ICD: 400 Bad Request: \"{\"error\":\"invalid_request\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/v2/mms/search"));
    }

    @Test
    void testSearchIcd_ServerError() throws Exception {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        // Mock token response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockTokenResponse)
                .addHeader("Content-Type", "application/json"));

        // Mock search server error response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500) // Internal Server Error
                .setBody("{\"error\":\"server_error\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/icd/entity/icd/release/11/v2/mms/search")
                        .param("q", "mockQuery")
                        .param("subtreeFilterUsesFoundationDescendants", "true")
                        .param("includeKeywordResult", "true")
                        .param("useFlexisearch", "true")
                        .param("flatResults", "true")
                        .param("highlightingEnabled", "true")
                        .param("medicalCodingMode", "true")
                        .header("Authorization", "Bearer mockToken123")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .header("API-Version", "v2"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()) // Expecting 500 Internal Server Error
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("Failed to get search ICD: 500 Internal Server Error: \"{\"error\":\"server_error\"}\""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiPath").value("uri=/api/icd/entity/icd/release/11/v2/mms/search"));
    }

}

