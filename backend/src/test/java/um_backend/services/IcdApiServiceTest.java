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
        String mockUri = "mock/uri";
        String mockResponse = "mockResponseData";

        when(mockIcdApiClient.getURI(mockUri)).thenReturn(mockResponse);
        String result = icdApiService.getIcdData(mockUri);

        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).getURI(mockUri);
    }

    @Test
    void testGetIcdData_UriException() {
        String mockUri = "mock/uri";
        when(mockIcdApiClient.getURI(mockUri)).thenThrow(new RuntimeException("URI retrieval failed"));

        assertThrows(Exception.class, () -> icdApiService.getIcdData(mockUri));
        verify(mockIcdApiClient).getURI(mockUri);
    }

    @Test
    void testGetIcdDetails_Success() throws Exception {
        String mockResponse = "mockIcdDetails";

        when(mockIcdApiClient.getIcdDetails()).thenReturn(mockResponse);
        String result = icdApiService.getIcdData();

        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).getIcdDetails();
    }

    @Test
    void testGetIcdDetails_Exception() throws Exception {
        when(mockIcdApiClient.getIcdDetails()).thenThrow(new RuntimeException("ICD Details retrieval failed"));

        assertThrows(RuntimeException.class, () -> icdApiService.getIcdData());
        verify(mockIcdApiClient).getIcdDetails();
    }

    @Test
    void testSearchIcd_Success() {
        // Arrange
        String query = "mockQuery";
        boolean subtreeFilterUsesFoundationDescendants = true;
        boolean includeKeywordResult = true;
        boolean useFlexisearch = true;
        boolean flatResults = true;
        boolean highlightingEnabled = true;
        boolean medicalCodingMode = true;
        String mockResponse = "mockSearchResult";

        when(mockIcdApiClient.searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult,
                useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode))
                .thenReturn(mockResponse);

        // Act
        String result = icdApiService.searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult, useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);

        // Assert
        assertEquals(mockResponse, result);
        verify(mockIcdApiClient).searchIcd(query, subtreeFilterUsesFoundationDescendants,
                includeKeywordResult, useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);
    }

    @Test
    void testSearchIcd_Exception() throws Exception {
        String query = "mockQuery";
        boolean subtreeFilterUsesFoundationDescendants = true;
        boolean includeKeywordResult = true;
        boolean useFlexisearch = true;
        boolean flatResults = true;
        boolean highlightingEnabled = true;
        boolean medicalCodingMode = true;
        when(mockIcdApiClient.searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult,
                useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode))
                .thenThrow(new RuntimeException("ICD Details retrieval failed"));

        assertThrows(RuntimeException.class, () -> icdApiService.searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult,
                useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode));
        verify(mockIcdApiClient).searchIcd(query, subtreeFilterUsesFoundationDescendants, includeKeywordResult,
                useFlexisearch, flatResults, highlightingEnabled, medicalCodingMode);
    }
}