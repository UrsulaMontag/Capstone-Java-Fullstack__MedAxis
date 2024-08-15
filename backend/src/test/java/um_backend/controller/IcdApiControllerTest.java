package um_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import um_backend.services.IcdApiService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IcdApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IcdApiService mockIcdApiService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("ENCRYPTION_PASSWORD", () -> "password");
        registry.add("ENCRYPTION_SALT", () -> "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e");
    }

    @Test
    void getIcdDetails_returnsIcdDetails() throws Exception {
        String mockCode = "A00";
        String mockResponse = "mock_response";

        when(mockIcdApiService.getIcdData(anyString())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/icd/entity/icd/release/11/v2/mms/" + mockCode))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));

        verify(mockIcdApiService, times(1)).getIcdData("https://id.who.int/icd/entity/" + mockCode);
    }

    @Test
    void getIcdDetails_ReturnsIcdDetails_withoutCode() throws Exception {
        String mockResponse = "mockIcdDetails";

        when(mockIcdApiService.getIcdData()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/icd/entity/icd/release/11/v2/mms"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));

        verify(mockIcdApiService, times(1)).getIcdData();
    }

    @Test
    void getToken_returnsToken() throws Exception {
        String mockToken = "mockToken";

        when(mockIcdApiService.getToken()).thenReturn(mockToken);

        mockMvc.perform(get("/api/icd/entity/icd/release/11/mms/token"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));

        verify(mockIcdApiService, times(1)).getToken();
    }

    @Test
    void searchIcd_returnsSearchResults() throws Exception {
        String mockQuery = "test";
        String mockResponse = "searchResults";

        when(mockIcdApiService.searchIcd(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/icd/entity/icd/release/11/v2/mms/search")
                        .param("q", mockQuery)
                        .param("subtreeFilterUsesFoundationDescendants", "false")
                        .param("includeKeywordResult", "true")
                        .param("useFlexisearch", "true")
                        .param("flatResults", "false")
                        .param("highlightingEnabled", "false")
                        .param("medicalCodingMode", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));

        verify(mockIcdApiService, times(1)).searchIcd(
                eq(mockQuery),
                eq(false),
                eq(true),
                eq(true),
                eq(false),
                eq(false),
                eq(true)
        );
    }
}