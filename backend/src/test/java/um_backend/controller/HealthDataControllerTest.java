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
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.services.HealthDataService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        String dataId = "newId";
        String icdCode = "ICD-10";
        List<String> icdCodes = new ArrayList<>() {{
            add(icdCode);
        }};
        HealthData healthData = new HealthData(dataId, icdCodes);

        when(mockHealthDataService.addOrUpdateIcdCodes(dataId, icdCode)).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data/" + "/" + dataId + "/add-icd-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(icdCode))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.icdCodes[0]").value("ICD-10"));
    }


    @Test
    void testGetHealthDataById() throws Exception {
        String dataId = "oldId";
        String icdCode = "ICD-10";


        HealthData healthData = new HealthData(dataId, List.of(icdCode));

        when(mockHealthDataService.getHealthDataById(dataId)).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/health_data/{dataId}", dataId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.icdCodes[0]").value(icdCode));
    }

    @Test
    void testGetHealthDataById_NotFound() throws Exception {
        String dataId = "invalidId";

        when(mockHealthDataService.getHealthDataById(dataId))
                .thenThrow(new InvalidIdException("Data with id " + dataId + " not found!"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/health_data/{dataId}", dataId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Test für das Erstellen von HealthData
    @Test
    void testCreateHealthData() throws Exception {
        String dataId = "newDataId";
        List<String> icdCodes = List.of("ICD-10");
        HealthData healthData = new HealthData(dataId, icdCodes);

        when(mockHealthDataService.createHealthData(any(HealthData.class))).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + dataId + "\", \"icdCodes\":[\"ICD-10\"]}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.icdCodes[0]").value("ICD-10"));
    }

    @Test
    void testCreateHealthData_NotFound() throws Exception {
        String dataId = "newDataId";
        List<String> icdCodes = List.of("ICD-10");
        HealthData healthData = new HealthData(dataId, icdCodes);

        when(mockHealthDataService.createHealthData(any(HealthData.class)))
                .thenThrow(new IllegalArgumentException("Failed creating health data."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + dataId + "\", \"icdCodes\":[\"ICD-10\"]}"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}