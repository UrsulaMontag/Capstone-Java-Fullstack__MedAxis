package um_backend.exeptions;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import um_backend.models.dto.PatientPersonalDTO;
import um_backend.services.PatientService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("ENCRYPTION_PASSWORD", () -> "password");
        registry.add("ENCRYPTION_SALT", () -> "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e");
    }

    @Test
    void testHandleNullPointerException() throws Exception {
        Mockito.when(patientService.createPatient(Mockito.any(PatientPersonalDTO.class))).thenThrow(new NullPointerException("This is a NullPointerException"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"testpatient\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorMsg").value("This is a NullPointerException"))
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.apiPath").exists());
    }

    @Test
    public void testHandleIdNotFoundException() throws Exception {
        // Wenn der Service mit der Fake-Datenbank (Mockito) aufgerufen wird und ein Item vom Typ DtoItem hinzugefügt
        // wird eine Exception geworfen
        Mockito.when(patientService.getPatientById(Mockito.any(String.class))).thenThrow(new InvalidIdException("This is a InvalidIdException"));

        // Fake-Post auf route /api/add
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("This is a InvalidIdException"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.apiPath").exists());
    }

    @Test
    void testHandleBadRequestException() throws Exception {
        Mockito.when(patientService.createPatient(Mockito.any(PatientPersonalDTO.class))).thenThrow(new BadRequestException("This is a BadRequestException"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"testpatient\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMsg").value("This is a BadRequestException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.apiPath").exists());
    }

    @Test
    void testHandleNotFoundException() throws Exception {
        Mockito.when(patientService.getPatientById(Mockito.any(String.class))).thenThrow(new NotFoundException("This is a NotFoundException"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMsg").value("This is a NotFoundException"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.apiPath").exists());
    }
}