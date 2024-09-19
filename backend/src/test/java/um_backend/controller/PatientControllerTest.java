package um_backend.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import um_backend.models.ContactInformation;
import um_backend.models.EmergencyContact;
import um_backend.models.Patient;
import um_backend.repository.PatientRepository;
import um_backend.services.EncryptionService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EncryptionService encryptionService;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("ENCRYPTION_PASSWORD", () -> "password");
        registry.add("ENCRYPTION_SALT", () -> "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e");
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb"); // H2 in-memory DB
        registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
    }

    @Test
    void getAllPatients_returnsListOfAllRegisteredPatients() throws Exception {
        // Encrypt data with the real EncryptionService
        List<Patient> encryptedPatients = List.of(
                new Patient("1",
                        encryptionService.encrypt("Erika"),
                        encryptionService.encrypt("Musterfrau"),
                        encryptionService.encrypt("1986-05-04"),
                        encryptionService.encrypt("Female"),
                        new EmergencyContact(
                                encryptionService.encrypt("John Doe"),
                                encryptionService.encrypt("Husband"),
                                encryptionService.encrypt("123456789")),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Single"),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Engineer"),
                        encryptionService.encrypt("12335467"),
                        new ContactInformation(
                                encryptionService.encrypt("0153476539"),
                                encryptionService.encrypt("test@email.com"),
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789101")),
                new Patient("2",
                        encryptionService.encrypt("Max"),
                        encryptionService.encrypt("Mustermann"),
                        encryptionService.encrypt("1999-05-16"),
                        encryptionService.encrypt("Male"),
                        new EmergencyContact(
                                encryptionService.encrypt("Jane Doe"),
                                encryptionService.encrypt("Mother"),
                                encryptionService.encrypt("987654321")),
                        encryptionService.encrypt("American"),
                        encryptionService.encrypt("Married"),
                        encryptionService.encrypt("English"),
                        encryptionService.encrypt("Doctor"),
                        encryptionService.encrypt("123495467"),
                        new ContactInformation(
                                null,
                                null,
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789145")));

        patientRepository.saveAll(encryptedPatients);

        // Test Request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                [
                                    {
                                        "id": "1",
                                        "firstname": "Erika",
                                        "lastname": "Musterfrau",
                                        "dateOfBirth": "1986-05-04",
                                        "gender": "Female",
                                        "emergencyContact": {
                                            "name": "John Doe",
                                            "relationship": "Husband",
                                            "phoneNumber": "123456789"
                                        },
                                        "nationality": "German",
                                        "maritalStatus": "Single",
                                        "primaryLanguage": "German",
                                        "occupation": "Engineer",
                                        "insuranceNr": "12335467",
                                        "contactInformation": {
                                            "phoneNr": "0153476539",
                                            "email": "test@email.com",
                                            "address": "Sesamstraße 56",
                                            "town": "68593 Teststadt"
                                        },
                                        "healthDataId": "123456789101"
                                    },
                                    {
                                        "id": "2",
                                        "firstname": "Max",
                                        "lastname": "Mustermann",
                                        "dateOfBirth": "1999-05-16",
                                        "gender": "Male",
                                        "emergencyContact": {
                                            "name": "Jane Doe",
                                            "relationship": "Mother",
                                            "phoneNumber": "987654321"
                                        },
                                        "nationality": "American",
                                        "maritalStatus": "Married",
                                        "primaryLanguage": "English",
                                        "occupation": "Doctor",
                                        "insuranceNr": "123495467",
                                        "contactInformation": {
                                            "phoneNr": "",
                                            "email": "",
                                            "address": "Sesamstraße 56",
                                            "town": "68593 Teststadt"
                                        },
                                        "healthDataId": "123456789145"
                                    }
                                ]
                        """));
    }

    @Test
    void getPatientById_returnsPatient_foundByGivenId() throws Exception {
        List<Patient> encryptedPatients = List.of(
                new Patient("1",
                        encryptionService.encrypt("Erika"),
                        encryptionService.encrypt("Musterfrau"),
                        encryptionService.encrypt("1986-05-04"),
                        encryptionService.encrypt("Female"),
                        new EmergencyContact(
                                encryptionService.encrypt("John Doe"),
                                encryptionService.encrypt("Husband"),
                                encryptionService.encrypt("123456789")),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Single"),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Engineer"),
                        encryptionService.encrypt("12335467"),
                        new ContactInformation(
                                encryptionService.encrypt("0153476539"),
                                encryptionService.encrypt("test@email.com"),
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789101")),
                new Patient("2",
                        encryptionService.encrypt("Max"),
                        encryptionService.encrypt("Mustermann"),
                        encryptionService.encrypt("1999-05-16"),
                        encryptionService.encrypt("Male"),
                        new EmergencyContact(
                                encryptionService.encrypt("Jane Doe"),
                                encryptionService.encrypt("Mother"),
                                encryptionService.encrypt("987654321")),
                        encryptionService.encrypt("American"),
                        encryptionService.encrypt("Married"),
                        encryptionService.encrypt("English"),
                        encryptionService.encrypt("Doctor"),
                        encryptionService.encrypt("123495467"),
                        new ContactInformation(
                                null,
                                null,
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789145")));

        patientRepository.saveAll(encryptedPatients);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                                "id": "1",
                                "firstname": "Erika",
                                "lastname": "Musterfrau",
                                "dateOfBirth": "1986-05-04",
                                "gender": "Female",
                                "emergencyContact": {
                                    "name": "John Doe",
                                    "relationship": "Husband",
                                    "phoneNumber": "123456789"
                                },
                                "nationality": "German",
                                "maritalStatus": "Single",
                                "primaryLanguage": "German",
                                "occupation": "Engineer",
                                "insuranceNr": "12335467",
                                "contactInformation": {
                                    "phoneNr": "0153476539",
                                    "email": "test@email.com",
                                    "address": "Sesamstraße 56",
                                    "town": "68593 Teststadt"
                                },
                                "healthDataId": "123456789101"
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
                            "gender": "Male",
                            "emergencyContact": {
                                "name": "Jane Doe",
                                "relationship": "Mother",
                                "phoneNumber": "987654321"
                            },
                            "nationality": "American",
                            "maritalStatus": "Married",
                            "primaryLanguage": "English",
                            "occupation": "Doctor",
                            "insuranceNr": "123495467",
                            "contactInformation": {
                                "phoneNr": "",
                                "email": "",
                                "address": "Sesamstraße 56",
                                "town": "68593 Teststadt"
                            },
                            "healthDataId": "123456789145",
                            "medicalExaminations": [{
                                "examinationDate": "2024-01-01",
                                "icdCodes": [{"code": "ICD-10", "description": "Description of ICD-10"}]
                            }]
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("Max"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value("Mustermann"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void updatePatient_returnsUpdatedPatient() throws Exception {
        List<Patient> encryptedPatients = List.of(
                new Patient("1",
                        encryptionService.encrypt("Erika"),
                        encryptionService.encrypt("Musterfrau"),
                        encryptionService.encrypt("1986-05-04"),
                        encryptionService.encrypt("Female"),
                        new EmergencyContact(
                                encryptionService.encrypt("John Doe"),
                                encryptionService.encrypt("Husband"),
                                encryptionService.encrypt("123456789")),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Single"),
                        encryptionService.encrypt("German"),
                        encryptionService.encrypt("Engineer"),
                        encryptionService.encrypt("12335467"),
                        new ContactInformation(
                                encryptionService.encrypt("0153476539"),
                                encryptionService.encrypt("test@email.com"),
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789101")),
                new Patient("2",
                        encryptionService.encrypt("Max"),
                        encryptionService.encrypt("Mustermann"),
                        encryptionService.encrypt("1999-05-16"),
                        encryptionService.encrypt("Male"),
                        new EmergencyContact(
                                encryptionService.encrypt("Jane Doe"),
                                encryptionService.encrypt("Mother"),
                                encryptionService.encrypt("987654321")),
                        encryptionService.encrypt("American"),
                        encryptionService.encrypt("Married"),
                        encryptionService.encrypt("English"),
                        encryptionService.encrypt("Doctor"),
                        encryptionService.encrypt("123495467"),
                        new ContactInformation(
                                null,
                                null,
                                encryptionService.encrypt("Sesamstraße 56"),
                                encryptionService.encrypt("68593 Teststadt")),
                        encryptionService.encrypt("123456789145")));

        patientRepository.saveAll(encryptedPatients);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/edit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstname": "Erika",
                            "lastname": "Musterfrau",
                            "dateOfBirth": "1986-05-04",
                            "gender": "Female",
                            "emergencyContact": {
                                "name": "John Doe",
                                "relationship": "Husband",
                                "phoneNumber": "123456789"
                            },
                            "nationality": "German",
                            "maritalStatus": "Single",
                            "primaryLanguage": "German",
                            "occupation": "Engineer",
                            "insuranceNr": "12335467",
                            "contactInformation": {
                                "phoneNr": "0153476539",
                                "email": "test@email.com",
                                "address": "Sesamstraße 56",
                                "town": "68593 Teststadt"
                            },
                            "healthDataId": "123456789101"
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": "1",
                            "firstname": "Erika",
                            "lastname": "Musterfrau",
                            "dateOfBirth": "1986-05-04",
                            "gender": "Female",
                            "emergencyContact": {
                                "name": "John Doe",
                                "relationship": "Husband",
                                "phoneNumber": "123456789"
                            },
                            "nationality": "German",
                            "maritalStatus": "Single",
                            "primaryLanguage": "German",
                            "occupation": "Engineer",
                            "insuranceNr": "12335467",
                            "contactInformation": {
                                "phoneNr": "0153476539",
                                "email": "test@email.com",
                                "address": "Sesamstraße 56",
                                "town": "68593 Teststadt"
                            },
                            "healthDataId": "123456789101"
                        }
                        """));
    }

    @Test
    void deletePatientById_deletesPatient_ByGivenId() throws Exception {
        patientRepository.save(new Patient("1",
                encryptionService.encrypt("Erika"),
                encryptionService.encrypt("Musterfrau"),
                encryptionService.encrypt("1986-05-04"),
                encryptionService.encrypt("Female"),
                new EmergencyContact(
                        encryptionService.encrypt("John Doe"),
                        encryptionService.encrypt("Husband"),
                        encryptionService.encrypt("123456789")),
                encryptionService.encrypt("German"),
                encryptionService.encrypt("Single"),
                encryptionService.encrypt("German"),
                encryptionService.encrypt("Engineer"),
                encryptionService.encrypt("12335467"),
                new ContactInformation(
                        encryptionService.encrypt("0153476539"),
                        encryptionService.encrypt("test@email.com"),
                        encryptionService.encrypt("Sesamstraße 56"),
                        encryptionService.encrypt("68593 Teststadt")),
                encryptionService.encrypt("123456789101")));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}