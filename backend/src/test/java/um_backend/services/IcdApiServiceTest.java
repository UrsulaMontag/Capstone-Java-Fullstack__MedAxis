package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetIcdData_Success() {
        // Arrange
        String mockToken = "mockToken123";
        String mockUri = "mock/uri";
        String mockResponse = "mockResponseData";

        when(mockIcdApiClient.getToken()).thenReturn(mockToken);
        when(mockIcdApiClient.getURI(mockToken, mockUri)).thenReturn(mockResponse);

        // Act
        String result = icdApiService.getIcdData(mockUri);

        // Assert
        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).getToken();
        verify(mockIcdApiClient).getURI(mockToken, mockUri);
    }

    @Test
    public void testGetIcdData_TokenException() {
        // Arrange
        String mockUri = "mock/uri";
        when(mockIcdApiClient.getToken()).thenThrow(new RuntimeException("Token retrieval failed"));

        // Act & Assert
        assertThrows(Exception.class, () -> icdApiService.getIcdData(mockUri));
        verify(mockIcdApiClient).getToken();
        verify(mockIcdApiClient, never()).getURI(anyString(), anyString());
    }

    @Test
    public void testGetIcdData_UriException() {
        // Arrange
        String mockToken = "mockToken123";
        String mockUri = "mock/uri";
        when(mockIcdApiClient.getToken()).thenReturn(mockToken);
        when(mockIcdApiClient.getURI(mockToken, mockUri)).thenThrow(new RuntimeException("URI retrieval failed"));

        // Act & Assert
        assertThrows(Exception.class, () -> icdApiService.getIcdData(mockUri));
        verify(mockIcdApiClient).getToken();
        verify(mockIcdApiClient).getURI(mockToken, mockUri);
    }
}