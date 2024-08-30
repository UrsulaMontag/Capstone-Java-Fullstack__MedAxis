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
import um_backend.models.*;
import um_backend.models.dto.HealthDataDto;
import um_backend.services.HealthDataService;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");
        List<MedicalExamination> medicalExaminations = new ArrayList<>() {{
            add(new MedicalExamination(LocalDateTime.now(), List.of(icdCode), "", "", List.of(""),
                    List.of(new Treatment("type", "description")),
                    List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
        }};
        HealthData healthData = new HealthData(dataId, "Female", 45, LocalDate.now(), medicalExaminations);
        when(mockHealthDataService.addIcdCodeToHealthData(dataId, icdCode)).thenReturn(healthData);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data/" + dataId + "/add-health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "code": "ICD-10",
                                    "description": "test-description"
                                    }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalExaminations[0].icdCodes[0].code").value("ICD-10"));
    }


    @Test
    void testGetHealthDataById() throws Exception {
        String dataId = "oldId";


        HealthData healthData = new HealthData(dataId, "Female", 45, LocalDate.now(), new ArrayList<>());

        when(mockHealthDataService.getHealthDataById(dataId)).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/health_data/{dataId}", dataId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId));
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
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");
        List<MedicalExamination> medicalExaminations = new ArrayList<>() {{
            add(new MedicalExamination(LocalDateTime.now(), List.of(icdCode), "", "", List.of(""),
                    List.of(new Treatment("type", "description")),
                    List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
        }};
        HealthData healthData = new HealthData(dataId, "Female", 45, LocalDate.now(), medicalExaminations);

        when(mockHealthDataService.createHealthData(any(HealthDataDto.class))).thenReturn(healthData);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"gender": "Female",
                                "ageAtFirstAdmission": 45,
                                "firstAdmissionDate": "2024-08-30",
                                "medicalExaminations": [
                                {
                                "examinationDate": "2024-08-30T14:57:52.353442",
                                "icdCodes": [
                                {
                                "code": "ICD-10",
                                "description": "test-description"
                                }
                                ],
                                "symptoms": "",
                                "diagnosis": "",
                                "medications": [],
                                "treatments": [],
                                "vitalSigns": [],
                                "additionalNotes": ""
                                }
                                ]
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(dataId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalExaminations[0].icdCodes[0].code").value("ICD-10"));
    }

    @Test
    void testCreateHealthData_NotFound() throws Exception {
        String dataId = "newDataId";

        when(mockHealthDataService.createHealthData(any(HealthDataDto.class)))
                .thenThrow(new IllegalArgumentException("Failed creating health data."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/health_data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + dataId + "\", \"icdCodes\":[\"ICD-10\"]}"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}