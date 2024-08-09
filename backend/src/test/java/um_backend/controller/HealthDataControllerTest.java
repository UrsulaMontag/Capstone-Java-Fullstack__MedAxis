package um_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import um_backend.models.HealthData;
import um_backend.services.HealthDataService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class HealthDataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthDataService mockHealthDataService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("ENCRYPTION_PASSWORD", () -> "password");
        registry.add("ENCRYPTION_SALT", () -> "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e");
    }

    @Test
    void testAddIcdCodeToPatient() throws Exception {
        String patientId = "patient1";
        String icdCode = "ICD-10";
        List<String> icdCodes = new ArrayList<>() {{
            add(icdCode);
        }};
        HealthData healthData = new HealthData("newId", patientId, icdCodes);

        when(mockHealthDataService.addOrUpdateHealthData(patientId, icdCode)).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data/" + patientId + "/add-icd-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(icdCode))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientId").value(patientId));
    }

    @Test
    void testGetHealthDataByPatientId() throws Exception {
        String patientId = "patient1";
        HealthData healthData = new HealthData("existingId", patientId, List.of("code1"));

        when(mockHealthDataService.getHealthDataByPatientId(patientId)).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/health_data/{patientId}", patientId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientId").value(patientId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.icdCodes[0]").value("code1"));
    }
}