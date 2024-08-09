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

        mockMvc.perform(get("/api/icd/" + mockCode))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));

        verify(mockIcdApiService, times(1)).getIcdData("https://id.who.int/icd/entity/" + mockCode);
    }
}