package um_backend.clients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(IcdApiClient.class)
class IcdApiClientTest {

    @Autowired
    private IcdApiClient icdApiClient;

    @Autowired
    private MockRestServiceServer mockServer;


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("icd.token.endpoint", () -> "https://example.com/token");
        registry.add("icd.client.id", () -> "test-client-id");
        registry.add("icd.client.secret", () -> "test-client-secret");
    }

    @BeforeEach
    void setUp() {
        this.mockServer = MockRestServiceServer.bindTo(RestClient.builder()).build();
        this.icdApiClient = new IcdApiClient();
    }

    @Test
    void testGetToken_Success() {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        mockServer.expect(once(), requestTo("https://example.com/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockTokenResponse));

    }

    @Test
    void testGetURI_Success() {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";
        String mockUriResponse = "{\"data\": \"someIcdData\"}";

        mockServer.expect(once(), requestTo("https://id.who.int/icd/entity/mock-code"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockTokenResponse));

        mockServer.expect(once(), requestTo("/api/icd"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer mockToken123"))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(header("API-Version", "v2"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockUriResponse));

    }

    @Test
    void testGetURI_Failure() {
        String mockTokenResponse = "{\"access_token\": \"mockToken123\"}";

        mockServer.expect(once(), requestTo("https://example.com/token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockTokenResponse));

        mockServer.expect(once(), requestTo("https://id.who.int/icd/entity/mock-code"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(IllegalArgumentException.class, () -> icdApiClient.getURI("https://id.who.int/icd/entity/mock-code"));
    }

    @Test
    void testGetIcdDetails_Success() {
        mockServer.expect(once(), requestTo("/api/icd"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer mockToken123"))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(header("API-Version", "v2"))
                .andRespond(withStatus(HttpStatus.OK));

    }
}