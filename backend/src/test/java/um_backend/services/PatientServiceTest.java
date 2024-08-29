package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.ContactInformation;
import um_backend.models.EmergencyContact;
import um_backend.models.HealthData;
import um_backend.models.Patient;
import um_backend.models.dto.HealthDataDto;
import um_backend.models.dto.PatientPersonalDTO;
import um_backend.repository.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientServiceTest {
    private PatientService patientService;
    private PatientRepository mockPatientRepository;
    private UtilService mockUtilService;
    private List<Patient> testPatientListEncrypted;
    private List<Patient> testPatientListDecrypted;
    private DataValidationService mockDataValidationService;
    private HealthDataService mockHealthDataService;

    @BeforeEach
    void setUp() {
        mockPatientRepository = mock(PatientRepository.class);
        mockUtilService = mock(UtilService.class);
        EncryptionService mockEncryptionService = mock(EncryptionService.class);
        mockDataValidationService = mock(DataValidationService.class);
        mockHealthDataService = mock(HealthDataService.class);
        patientService = new PatientService(mockPatientRepository, mockUtilService, mockDataValidationService, mockEncryptionService, mockHealthDataService);
        // Dummy encryption/decryption values
        when(mockEncryptionService.encrypt("Erika")).thenReturn("encryptedErika");
        when(mockEncryptionService.encrypt("Musterfrau")).thenReturn("encryptedMusterfrau");
        when(mockEncryptionService.encrypt("1986-05-04")).thenReturn("encryptedDate");
        when(mockEncryptionService.encrypt("Female")).thenReturn("encryptedFemGender");
        when(mockEncryptionService.encrypt("Harald Musterfrau")).thenReturn("encryptedEmergencyContactName");
        when(mockEncryptionService.encrypt("Husband")).thenReturn("encryptedEmergencyContactRelationship");
        when(mockEncryptionService.encrypt("01785469875")).thenReturn("encryptedEmergencyContactPhone");
        when(mockEncryptionService.encrypt("German")).thenReturn("encryptedNationality");
        when(mockEncryptionService.encrypt("Married")).thenReturn("encryptedMaritalStatus");
        when(mockEncryptionService.encrypt("German")).thenReturn("encryptedPrimeLanguage");
        when(mockEncryptionService.encrypt("Engineer")).thenReturn("encryptedOccupation");
        when(mockEncryptionService.encrypt("12335467")).thenReturn("encryptedInsuranceNr");
        when(mockEncryptionService.encrypt("Sesamstraße 56")).thenReturn("encryptedAddress");
        when(mockEncryptionService.encrypt("68593 Teststadt")).thenReturn("encryptedTown");
        when(mockEncryptionService.encrypt("0153476539")).thenReturn("encryptedPhoneNr");
        when(mockEncryptionService.encrypt("test@email.com")).thenReturn("encryptedEmail");
        when(mockEncryptionService.encrypt("123456789101")).thenReturn("encryptedHealthDataId");


        when(mockEncryptionService.decrypt("encryptedErika")).thenReturn("Erika");
        when(mockEncryptionService.decrypt("encryptedMusterfrau")).thenReturn("Musterfrau");
        when(mockEncryptionService.decrypt("encryptedDate")).thenReturn("1986-05-04");
        when(mockEncryptionService.decrypt("encryptedFemGender")).thenReturn("Female");
        when(mockEncryptionService.decrypt("encryptedEmergencyContactName")).thenReturn("Harald Musterfrau");
        when(mockEncryptionService.decrypt("encryptedEmergencyContactRelationship")).thenReturn("Husband");
        when(mockEncryptionService.decrypt("encryptedEmergencyContactPhone")).thenReturn("01785469875");
        when(mockEncryptionService.decrypt("encryptedNationality")).thenReturn("German");
        when(mockEncryptionService.decrypt("encryptedMaritalStatus")).thenReturn("Married");
        when(mockEncryptionService.decrypt("encryptedPrimeLanguage")).thenReturn("German");
        when(mockEncryptionService.decrypt("encryptedOccupation")).thenReturn("Engineer");
        when(mockEncryptionService.decrypt("encryptedInsuranceNr")).thenReturn("12335467");
        when(mockEncryptionService.decrypt("encryptedAddress")).thenReturn("Sesamstraße 56");
        when(mockEncryptionService.decrypt("encryptedTown")).thenReturn("68593 Teststadt");
        when(mockEncryptionService.decrypt("encryptedPhoneNr")).thenReturn("0153476539");
        when(mockEncryptionService.decrypt("encryptedEmail")).thenReturn("test@email.com");
        when(mockEncryptionService.decrypt("encryptedHealthDataId")).thenReturn("123456789101");


        testPatientListEncrypted = new ArrayList<>() {{
            add(new Patient("2",
                    mockEncryptionService.encrypt("Erika"),
                    mockEncryptionService.encrypt("Musterfrau"),
                    mockEncryptionService.encrypt("1986-05-04"),
                    mockEncryptionService.encrypt("Female"),
                    new EmergencyContact(
                            mockEncryptionService.encrypt("Harald Musterfrau"),
                            mockEncryptionService.encrypt("Husband"),
                            mockEncryptionService.encrypt("01785469875")),
                    mockEncryptionService.encrypt("German"),
                    mockEncryptionService.encrypt("Married"),
                    mockEncryptionService.encrypt("German"),
                    mockEncryptionService.encrypt("Engineer"),
                    mockEncryptionService.encrypt("12335467"),
                    new ContactInformation(
                            mockEncryptionService.encrypt("0153476539"),
                            mockEncryptionService.encrypt("test@email.com"),
                            mockEncryptionService.encrypt("Sesamstraße 56"),
                            mockEncryptionService.encrypt("68593 Teststadt")),
                    mockEncryptionService.encrypt("123456789101")));
        }};

        testPatientListDecrypted = new ArrayList<>() {{
            add(new Patient("2", "Erika", "Musterfrau", "1986-05-04", "Female",
                    new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                    "German", "Married", "German", "Engineer", "12335467",
                    new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"),
                    "123456789101"));
        }};

        when(mockDataValidationService.isValidEmail(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth(any(String.class))).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidName(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidGender(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidNationality(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidMaritalStatus(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidPrimaryLanguage(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidOccupation(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidEmergencyContact(any(EmergencyContact.class))).thenReturn(true);
    }

    @Test
    void getAllPatients_returnsAllRegisteredPatients() {
        when(mockPatientRepository.findAll()).thenReturn(testPatientListEncrypted);
        List<Patient> actual = patientService.getAllPatients();
        verify(mockPatientRepository).findAll();
        assertNotEquals(testPatientListEncrypted, actual);
        assertEquals(testPatientListDecrypted, actual);
    }

    @Test
    void getAllPatients_throwsException_whenWentWrong() {
        when(mockPatientRepository.findAll()).thenThrow(new RuntimeException("Error getting all patients"));
        try {
            patientService.getAllPatients();
            verify(mockPatientRepository).findAll();
            verify(patientService).getAllPatients();
            fail("Expected exception, but was not thrown");
        } catch (Exception e) {
            assertEquals("Error getting all patients", e.getMessage());
        }
    }

    @Test
    void getPatientById_returnsPatient_withGivenId() throws InvalidIdException {
        when(mockPatientRepository.findById("2")).thenReturn(Optional.ofNullable(testPatientListEncrypted.getFirst()));
        Patient actual = patientService.getPatientById("2");
        verify(mockPatientRepository).findById("2");
        assertNotEquals(testPatientListEncrypted.getFirst(), actual);
        assertEquals(testPatientListDecrypted.getFirst(), actual);
    }

    @Test
    void getPatientById_throwsException_whenPatientNotFound() {
        when(mockPatientRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> patientService.getPatientById("2"));
        verify(mockPatientRepository).findById("2");
    }

    @Test
    void createPatient_returnsPatient_whenPatientIsCreated() {
        PatientPersonalDTO newPatient = new PatientPersonalDTO(
                testPatientListDecrypted.getFirst().firstname(), testPatientListDecrypted.getFirst().lastname(), testPatientListDecrypted.getFirst().dateOfBirth(),
                testPatientListDecrypted.getFirst().gender(), testPatientListDecrypted.getFirst().emergencyContact(), testPatientListDecrypted.getFirst().nationality(),
                testPatientListDecrypted.getFirst().maritalStatus(), testPatientListDecrypted.getFirst().primaryLanguage(), testPatientListDecrypted.getFirst().occupation(),
                testPatientListDecrypted.getFirst().insuranceNr(), testPatientListDecrypted.getFirst().contactInformation());
        Patient expectedPatient = testPatientListEncrypted.getFirst();
        HealthDataDto newHealthData = new HealthDataDto(new ArrayList<>());

        when(mockUtilService.generateId()).thenReturn("2");
        when(mockPatientRepository.save(expectedPatient)).thenReturn(expectedPatient);
        when(mockHealthDataService.createHealthData(newHealthData)).thenReturn(new HealthData("123456789101", newHealthData.icdCodes()));
        patientService.createPatient(newPatient);
        verify(mockPatientRepository).save(expectedPatient);
        verify(mockHealthDataService).createHealthData(newHealthData);
        verify(mockUtilService).generateId();
    }

    @Test
    void createPatient_shouldThrowException_WhenWentWrong() {
        HealthDataDto newHealthData = new HealthDataDto(new ArrayList<>());
        when(mockHealthDataService.createHealthData(newHealthData)).thenReturn(new HealthData("2", newHealthData.icdCodes()));

        when(mockPatientRepository.save(any(Patient.class))).thenThrow(new IllegalArgumentException("Error message"));
        PatientPersonalDTO newPatient = new PatientPersonalDTO(testPatientListDecrypted.getFirst().firstname(), testPatientListDecrypted.getFirst().lastname(), testPatientListDecrypted.getFirst().dateOfBirth(),
                testPatientListDecrypted.getFirst().gender(), testPatientListDecrypted.getFirst().emergencyContact(), testPatientListDecrypted.getFirst().nationality(),
                testPatientListDecrypted.getFirst().maritalStatus(), testPatientListDecrypted.getFirst().primaryLanguage(), testPatientListDecrypted.getFirst().occupation(),
                testPatientListDecrypted.getFirst().insuranceNr(), testPatientListDecrypted.getFirst().contactInformation());
        try {
            patientService.createPatient(newPatient);
            verify(mockPatientRepository).save(any(Patient.class));
            fail("Expected exception, but was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message", e.getMessage());
        }
    }

    @Test
    void updatePatient_returnsPatient_whenPatientIsUpdated() throws InvalidIdException {
        PatientPersonalDTO updateDto = new PatientPersonalDTO(
                testPatientListDecrypted.getFirst().firstname(), testPatientListDecrypted.getFirst().lastname(), testPatientListDecrypted.getFirst().dateOfBirth(),
                testPatientListDecrypted.getFirst().gender(), testPatientListDecrypted.getFirst().emergencyContact(), testPatientListDecrypted.getFirst().nationality(),
                testPatientListDecrypted.getFirst().maritalStatus(), testPatientListDecrypted.getFirst().primaryLanguage(), testPatientListDecrypted.getFirst().occupation(),
                testPatientListDecrypted.getFirst().insuranceNr(), testPatientListDecrypted.getFirst().contactInformation());
        Patient actualPatient = testPatientListDecrypted.getFirst();

        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientListEncrypted.getFirst()));
        when(mockPatientRepository.save(any(Patient.class))).thenReturn(actualPatient);
        patientService.updatePatientById("2", updateDto);
        verify(mockPatientRepository).findById("2");
        verify(mockPatientRepository).save(any(Patient.class));
        assertEquals(testPatientListDecrypted.getFirst(), actualPatient);
        assertNotEquals(testPatientListEncrypted.getFirst(), actualPatient);
    }

    @Test
    void updatePatient_throwsException_whenPatientNotFound() {
        when(mockPatientRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> patientService.updatePatientById("2", new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467", new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"))));
        verify(mockPatientRepository).findById("2");
    }

    @Test
    void deletePatient_deletesPatient_withGivenId() throws InvalidIdException {
        when(mockPatientRepository.existsById("2")).thenReturn(true);
        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientListEncrypted.getFirst()));
        patientService.deletePatientById("2");
        verify(mockPatientRepository, times(1)).deleteById("2");
    }

    @Test
    void deletePatient_throwsException_whenPatientNotFound() {
        when(mockPatientRepository.existsById("2")).thenReturn(false);
        when(mockPatientRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> patientService.deletePatientById("2"));
        verify(mockPatientRepository, times(1)).existsById("2");
    }

    @Test
    void decryptPatient_shouldDecryptPatientData() {
        Patient encryptedPatient = testPatientListEncrypted.getFirst();

        Patient decryptedPatient = patientService.decryptPatient(encryptedPatient);

        Patient expectedPatient = testPatientListDecrypted.getFirst();
        assertEquals(expectedPatient, decryptedPatient);
    }

    @Test
    void createOrUpdatePatient_createsNewPatient_whenExistingPatientIsNull() {
        PatientPersonalDTO dto = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));

        Patient newPatient = testPatientListEncrypted.getFirst();
        HealthDataDto newHealthData = new HealthDataDto(new ArrayList<>());


        when(mockUtilService.generateId()).thenReturn("2");
        when(mockHealthDataService.createHealthData(newHealthData)).thenReturn(new HealthData("123456789101", newHealthData.icdCodes()));

        Patient result = patientService.createOrUpdatePatient(dto, null);

        verify(mockUtilService).generateId();
        verify(mockHealthDataService).createHealthData(newHealthData);
        assertEquals(newPatient, result);
    }

    @Test
    void createOrUpdatePatient_updatesExistingPatient_whenExistingPatientIsNotNull() {
        PatientPersonalDTO dto = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));

        Patient updatedPatient = testPatientListEncrypted.getFirst();
        Patient result = patientService.createOrUpdatePatient(dto, updatedPatient);

        assertEquals(updatedPatient, result);
    }

    @Test
    void createEncryptedContactInformation_createsEncryptedContactInformation() {
        ContactInformation contact = new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt");

        ContactInformation encryptedContact = new ContactInformation(
                "encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown"
        );


        ContactInformation result = patientService.createEncryptedContactInformation(contact);

        assertEquals(encryptedContact, result);
    }

    @Test
    void validatePatientData_validData_shouldNotThrowException() {
        PatientPersonalDTO validPatient = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));

        assertDoesNotThrow(() -> patientService.validatePatientData(validPatient));
    }

    @Test
    void validatePatientData_invalidName_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO("invalid name", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));

        when(mockDataValidationService.isValidName("invalid name")).thenReturn(false);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("invalid-insurance")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid name format.", thrown.getMessage());
    }

    @Test
    void validatePatientData_invalidInsuranceNumber_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "invalid-insurance",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));


        when(mockDataValidationService.isValidName("Erika")).thenReturn(true);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("invalid-insurance")).thenReturn(false);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid insurance number format.", thrown.getMessage());
    }

    @Test
    void validatePatientData_invalidDateOfBirth_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO("Erika", "Musterfrau", "invalid-date", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));


        when(mockDataValidationService.isValidName("Erika")).thenReturn(true);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("invalid-date")).thenReturn(false);
        when(mockDataValidationService.isValidInsuranceNumber("12335467")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid date of birth.", thrown.getMessage());
    }

    @Test
    void validatePatientData_invalidPhoneNumber_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("invalid-phone", "test@email.com", "Sesamstraße 56", "68593 Teststadt"));


        when(mockDataValidationService.isValidName("Erika")).thenReturn(true);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("12335467")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("invalid-phone")).thenReturn(false);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid phone number format.", thrown.getMessage());
    }

    @Test
    void validatePatientData_invalidEmail_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO("Erika", "Musterfrau", "1986-05-04", "Female",
                new EmergencyContact("Harald Musterfrau", "Husband", "01785469875"),
                "German", "Married", "German", "Engineer", "12335467",
                new ContactInformation("0153476539", "invalid-email", "Sesamstraße 56", "68593 Teststadt"));


        when(mockDataValidationService.isValidName("Erika")).thenReturn(true);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("12335467")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("invalid-email")).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid email address format.", thrown.getMessage());
    }
}
