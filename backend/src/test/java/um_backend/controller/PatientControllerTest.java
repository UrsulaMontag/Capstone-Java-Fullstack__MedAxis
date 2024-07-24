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
import um_backend.models.Patient;
import um_backend.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void getAllPatients_returnsListOfAllRegisteredPatients() throws Exception {
        patientRepository.saveAll(List.of(
                (new Patient("1", "Max", "Mustermann", LocalDate.of(2001, 4, 12))),
                (new Patient("2", "Erika", "Musterfrau", LocalDate.of(1986, 5, 4))),
                (new Patient("3", "Gerlinde", "Häberle", LocalDate.of(1998, 4, 16)))
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                            [
                                {
                                "id": "1",
                                "firstname": "Max",
                                "lastname": "Mustermann",
                                "dateOfBirth": "2001-04-12"
                                },
                                {
                                "id": "2",
                                "firstname": "Erika",
                                "lastname": "Musterfrau",
                                "dateOfBirth": "1986-05-04"
                                },
                                {
                                "id": "3",
                                "firstname": "Gerlinde",
                                "lastname": "Häberle",
                                "dateOfBirth": "1998-04-16"
                                }
                            ]
                        """));
    }

    @Test
    void getPatientById_returnsPatient_foundByGivenId() throws Exception {
        patientRepository.saveAll(List.of(
                (new Patient("2", "Erika", "Musterfrau", LocalDate.of(1986, 5, 4)))
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "2",
                            "firstname": "Erika",
                            "lastname": "Musterfrau",
                            "dateOfBirth": "1986-05-04"
                        }
                        """));
    }

    @Test
    void createPatient_returnsNewPatient_withRandomId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstname": "Max",
                                 "lastname": "Mustermann",
                                 "dateOfBirth": "1984-02-25"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {"firstname": "Max",
                         "lastname": "Mustermann",
                         "dateOfBirth": "1984-02-25"}
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void updatePatient_returnsUpdatedPatient() throws Exception {
        patientRepository.saveAll(List.of(
                (new Patient("2", "Erika", "Musterfrau", LocalDate.of(1986, 5, 4)))
        ));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/edit/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "Erika",
                                "lastname": "Müller",
                                "dateOfBirth": "1986-05-04"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                        "id": "2",
                        "firstname": "Erika",
                        "lastname": "Müller",
                        "dateOfBirth": "1986-05-04"
                        }
                        """));
    }

    @Test
    void deletePatientById_deletesPatient_ByGivenId() throws Exception {
        patientRepository.save((new Patient("2", "Erika", "Musterfrau", LocalDate.of(1986, 5, 4))));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}