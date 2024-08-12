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
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIcdData_Success() {
        // Arrange
        String mockUri = "mock/uri";
        String mockResponse = "mockResponseData";

        when(mockIcdApiClient.getURI(mockUri)).thenReturn(mockResponse);

        // Act
        String result = icdApiService.getIcdData(mockUri);

        // Assert
        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).getURI(mockUri);
    }

    @Test
    void testGetIcdData_UriException() {
        // Arrange
        String mockUri = "mock/uri";
        when(mockIcdApiClient.getURI(mockUri)).thenThrow(new RuntimeException("URI retrieval failed"));

        // Act & Assert
        assertThrows(Exception.class, () -> icdApiService.getIcdData(mockUri));
        verify(mockIcdApiClient).getURI(mockUri);
    }

    @Test
    void testGetIcdDetails_Success() throws Exception {
        // Arrange
        String mockResponse = "mockIcdDetails";

        when(mockIcdApiClient.getIcdDetails()).thenReturn(mockResponse);

        // Act
        String result = icdApiService.getIcdData();

        // Assert
        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).getIcdDetails();
    }

    @Test
    void testGetIcdDetails_Exception() throws Exception {
        // Arrange
        when(mockIcdApiClient.getIcdDetails()).thenThrow(new RuntimeException("ICD Details retrieval failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> icdApiService.getIcdData());
        verify(mockIcdApiClient).getIcdDetails();
    }
}