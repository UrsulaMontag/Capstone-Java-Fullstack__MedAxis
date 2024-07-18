package um_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import um_backend.repository.PatientRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientRepository patientRepository;

    @Test
    void createPatient_returnsNewPatient_withRandomId() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                    "firstname": "Max",
                                    "lastname": "Mustermann",
                                    "dateOfBirth": "1984-02-25"}
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                            "firstname": "Max",
                            "lastname": "Mustermann",
                            "dateOfBirth": "1984-02-25"}
                            """))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}