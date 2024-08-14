package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import um_backend.clients.IcdApiClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcdApiServiceTest {

    @InjectMocks
    private IcdApiService icdApiService;

    @Mock
    private IcdApiClient mockIcdApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIcdDataByUri_Success() {
        String mockUri = "https://id.who.int/icd/entity/mock-code";
        String mockResponse = "{\"data\": \"mockIcdData\"}";

        when(mockIcdApiClient.getURI(mockUri)).thenReturn(mockResponse);

        String result = icdApiService.getIcdData(mockUri);

        assertEquals(mockResponse, result);
        verify(mockIcdApiClient, times(1)).getURI(mockUri);
    }

    @Test
    void testGetIcdDataByUri_Failure() {
        String mockUri = "https://id.who.int/icd/entity/mock-code";

        when(mockIcdApiClient.getURI(mockUri)).thenThrow(new IllegalArgumentException("Invalid URI"));

        assertThrows(IllegalArgumentException.class, () -> icdApiService.getIcdData(mockUri));
        verify(mockIcdApiClient, times(1)).getURI(mockUri);
    }

    @Test
    void testGetIcdData_Success() throws Exception {
        String mockResponse = "{\"data\": \"mockIcdDetails\"}";

        when(mockIcdApiClient.getIcdDetails()).thenReturn(mockResponse);

        String result = icdApiService.getIcdData();

        assertEquals(mockResponse, result);
        verify(mockIcdApiClient, times(1)).getIcdDetails();
    }

    @Test
    void testGetIcdData_Failure() throws Exception {
        when(mockIcdApiClient.getIcdDetails()).thenThrow(new RuntimeException("Service unavailable"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> icdApiService.getIcdData());

        assertEquals("Failed to get ICD details", exception.getMessage());
        verify(mockIcdApiClient, times(1)).getIcdDetails();
    }

    @Test
    void testGetToken_Success() {
        String mockToken = "mockToken123";

        when(mockIcdApiClient.getToken()).thenReturn(mockToken);

        String result = icdApiService.getToken();

        assertEquals(mockToken, result);
        verify(mockIcdApiClient, times(1)).getToken();
    }

    @Test
    void testGetToken_Failure() {
        when(mockIcdApiClient.getToken()).thenThrow(new RuntimeException("Token fetch failed"));

        String result = icdApiService.getToken();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), result);
        verify(mockIcdApiClient, times(1)).getToken();
    }

    @Test
    void testSearchIcd_Success() {
        String mockQuery = "mockQuery";
        String mockSearchResponse = "{\"results\": [\"result1\", \"result2\"]}";

        when(mockIcdApiClient.searchIcd(mockQuery, true, true, true, true, true, true)).thenReturn(mockSearchResponse);

        String result = icdApiService.searchIcd(mockQuery, true, true, true, true, true, true);

        assertEquals(mockSearchResponse, result);
        verify(mockIcdApiClient, times(1)).searchIcd(mockQuery, true, true, true, true, true, true);
    }

    @Test
    void testSearchIcd_Failure() {
        String mockQuery = "mockQuery";

        when(mockIcdApiClient.searchIcd(mockQuery, true, true, true, true, true, true))
                .thenThrow(new RuntimeException("Search failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> icdApiService.searchIcd(mockQuery, true, true, true, true, true, true));

        assertEquals("Failed to get search details", exception.getMessage());
        verify(mockIcdApiClient, times(1)).searchIcd(mockQuery, true, true, true, true, true, true);
    }
}