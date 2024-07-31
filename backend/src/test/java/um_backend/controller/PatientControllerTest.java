package um_backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import um_backend.models.ContactInformation;
import um_backend.models.Patient;
import um_backend.repository.PatientRepository;
import um_backend.services.EncryptionService;

import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;
    @MockBean
    private EncryptionService encryptionService;


    @BeforeEach
    void setUp() {
        // Mocked encryption and decryption for the test
        when(encryptionService.encrypt("Erika")).thenReturn("encryptedErika");
        when(encryptionService.encrypt("Musterfrau")).thenReturn("encryptedMusterfrau");
        when(encryptionService.encryptDate("1986-05-04")).thenReturn("encryptedDate");
        when(encryptionService.encrypt("12335467")).thenReturn("encryptedInsuranceNr");
        when(encryptionService.encrypt("Sesamstraße 56")).thenReturn("encryptedAddress");
        when(encryptionService.encrypt("68593 Teststadt")).thenReturn("encryptedTown");
        when(encryptionService.encrypt("0153476539")).thenReturn("encryptedPhoneNr");
        when(encryptionService.encrypt("test@email.com")).thenReturn("encryptedEmail");

        // Mock decryption
        when(encryptionService.decrypt("encryptedErika")).thenReturn("Erika");
        when(encryptionService.decrypt("encryptedMusterfrau")).thenReturn("Musterfrau");
        when(encryptionService.decryptDate("encryptedDate")).thenReturn("1986-05-04");
        when(encryptionService.decrypt("encryptedInsuranceNr")).thenReturn("12335467");
        when(encryptionService.decrypt("encryptedAddress")).thenReturn("Sesamstraße 56");
        when(encryptionService.decrypt("encryptedTown")).thenReturn("68593 Teststadt");
        when(encryptionService.decrypt("encryptedPhoneNr")).thenReturn("0153476539");
        when(encryptionService.decrypt("encryptedEmail")).thenReturn("test@email.com");

        when(encryptionService.encrypt("Max")).thenReturn("encryptedMax");
        when(encryptionService.encrypt("Mustermann")).thenReturn("encryptedMustermann");
        when(encryptionService.encryptDate("1999-05-16")).thenReturn("encryptedDate1");
        when(encryptionService.encrypt("123495467")).thenReturn("encryptedInsuranceNr1");

        when(encryptionService.decrypt("encryptedMax")).thenReturn("Max");
        when(encryptionService.decrypt("encryptedMustermann")).thenReturn("Mustermann");
        when(encryptionService.decryptDate("encryptedDate1")).thenReturn("1999-05-16");
        when(encryptionService.decrypt("encryptedInsuranceNr1")).thenReturn("123495467");
    }

    @Test
    void getAllPatients_returnsListOfAllRegisteredPatients() throws Exception {
        List<Patient> encryptedPatients = List.of(
                new Patient("1", "encryptedErika", "encryptedMusterfrau",
                        "encryptedDate", "encryptedInsuranceNr",
                        new ContactInformation("encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown")),
                new Patient("2", "encryptedMax", "encryptedMustermann",
                        "encryptedDate1", "encryptedInsuranceNr1",
                        new ContactInformation(null, null, "encryptedAddress", "encryptedTown"))
        );

        patientRepository.saveAll(encryptedPatients);

        // Test-Request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                            [
                                                {
                                                    "id": "1",
                                                    "firstname": "Erika",
                                                    "lastname": "Musterfrau",
                                                    "dateOfBirth": "1986-05-04",
                                                    "insuranceNr": "12335467",
                                                    "contactInformation": {
                                                        "phoneNr": "0153476539",
                                                        "email":"test@email.com",
                                                        "address":"Sesamstraße 56",
                                                        "town":"68593 Teststadt"
                                                    }
                                                },
                        {
                                                    "id": "2",
                                                    "firstname": "Max",
                                                    "lastname": "Mustermann",
                                                    "dateOfBirth": "1999-05-16",
                                                    "insuranceNr": "123495467",
                                                    "contactInformation": {
                                                        "phoneNr": "",
                                                        "email":"",
                                                        "address":"Sesamstraße 56",
                                                        "town":"68593 Teststadt"
                                                    }
                                                }
                                            ]
                        """));
    }

    @Test
    void getPatientById_returnsPatient_foundByGivenId() throws Exception {
        List<Patient> encryptedPatients = List.of(
                new Patient("1", "encryptedErika", "encryptedMusterfrau",
                        "encryptedDate", "encryptedInsuranceNr",
                        new ContactInformation("encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown")),
                new Patient("2", "encryptedMax", "encryptedMustermann",
                        "encryptedDate1", "encryptedInsuranceNr1",
                        new ContactInformation(null, null, "encryptedAddress", "encryptedTown"))
        );

        patientRepository.saveAll(encryptedPatients);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "1",
                            "firstname": "Erika",
                            "lastname": "Musterfrau",
                            "dateOfBirth": "1986-05-04",
                            "insuranceNr": "12335467",
                            "contactInformation": {
                                "phoneNr": "0153476539",
                                "email": "test@email.com",
                                "address": "Sesamstraße 56",
                                "town": "68593 Teststadt"
                            }
                        }
                        """));
    }

    @Test
    void createPatient_returnsNewPatient_withRandomId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "Max",
                                "lastname": "Mustermann",
                                "dateOfBirth": "1999-05-16",
                                "insuranceNr": "123495467",
                                "contactInformation": { "address":"Sesamstraße 56",
                                             "town":"68593 Teststadt"
                                                      }
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                        "firstname": "encryptedMax",
                        "lastname": "encryptedMustermann",
                        "dateOfBirth": "encryptedDate1",
                        "insuranceNr": "encryptedInsuranceNr1",
                        "contactInformation": {
                                     "phoneNr": "",
                                     "email": "",
                                     "address":"encryptedAddress",
                                     "town":"encryptedTown"
                                              }
                        }
                        """))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void updatePatient_returnsUpdatedPatient() throws Exception {
        patientRepository.save(new Patient("1", "encryptedErika", "encryptedMusterfrau",
                "encryptedDate", "encryptedInsuranceNr",
                new ContactInformation("encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown")));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstname": "Erika",
                                "lastname": "Musterfrau",
                                "dateOfBirth": "1986-05-04",
                                "insuranceNr": "12335467",
                                "contactInformation": { "address":"Sesamstraße 56",
                                     "town":"68593 Teststadt",
                                     "phoneNr": "0153476539",
                                     "email": "test@email.com"
                                              }
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                            {
                        "firstname": "encryptedErika",
                        "lastname": "encryptedMusterfrau",
                        "dateOfBirth": "encryptedDate",
                        "insuranceNr": "encryptedInsuranceNr",
                        "contactInformation": {
                                     "phoneNr": "encryptedPhoneNr",
                                     "email": "encryptedEmail",
                                     "address":"encryptedAddress",
                                     "town":"encryptedTown"
                                              }
                        }
                        """));
    }

    @Test
    void deletePatientById_deletesPatient_ByGivenId() throws Exception {
        patientRepository.save(new Patient("2", "Erika", "Musterfrau", "encr1986-05-04", "12335467", new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt")));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}