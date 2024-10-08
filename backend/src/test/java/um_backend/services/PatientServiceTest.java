package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.ContactInformation;
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
        when(mockEncryptionService.encrypt("12335467")).thenReturn("encryptedInsuranceNr");
        when(mockEncryptionService.encrypt("Sesamstraße 56")).thenReturn("encryptedAddress");
        when(mockEncryptionService.encrypt("68593 Teststadt")).thenReturn("encryptedTown");
        when(mockEncryptionService.encrypt("0153476539")).thenReturn("encryptedPhoneNr");
        when(mockEncryptionService.encrypt("test@email.com")).thenReturn("encryptedEmail");
        when(mockEncryptionService.encrypt("2")).thenReturn("encryptedHealthDataId");


        when(mockEncryptionService.decrypt("encryptedErika")).thenReturn("Erika");
        when(mockEncryptionService.decrypt("encryptedMusterfrau")).thenReturn("Musterfrau");
        when(mockEncryptionService.decrypt("encryptedDate")).thenReturn("1986-05-04");
        when(mockEncryptionService.decrypt("encryptedInsuranceNr")).thenReturn("12335467");
        when(mockEncryptionService.decrypt("encryptedAddress")).thenReturn("Sesamstraße 56");
        when(mockEncryptionService.decrypt("encryptedTown")).thenReturn("68593 Teststadt");
        when(mockEncryptionService.decrypt("encryptedPhoneNr")).thenReturn("0153476539");
        when(mockEncryptionService.decrypt("encryptedEmail")).thenReturn("test@email.com");
        when(mockEncryptionService.decrypt("encryptedHealthDataId")).thenReturn("2");

        when(mockEncryptionService.encrypt("Max")).thenReturn("encryptedMax");
        when(mockEncryptionService.encrypt("Mustermann")).thenReturn("encryptedMustermann");
        when(mockEncryptionService.encrypt("1999-05-16")).thenReturn("encryptedDate1");
        when(mockEncryptionService.encrypt("123495467")).thenReturn("encryptedInsuranceNr1");
        when(mockEncryptionService.encrypt("1")).thenReturn("encryptedHealthDataId1");

        when(mockEncryptionService.decrypt("encryptedMax")).thenReturn("Max");
        when(mockEncryptionService.decrypt("encryptedMustermann")).thenReturn("Mustermann");
        when(mockEncryptionService.decrypt("encryptedDate1")).thenReturn("1999-05-16");
        when(mockEncryptionService.decrypt("encryptedInsuranceNr1")).thenReturn("123495467");
        when(mockEncryptionService.decrypt("encryptedHealthDataId1")).thenReturn("1");


        testPatientListEncrypted = new ArrayList<>() {{
            add(new Patient("1", "encryptedMax", "encryptedMustermann", "encryptedDate1", "encryptedInsuranceNr1",
                    new ContactInformation(null, null, "encryptedAddress", "encryptedTown"), "encryptedHealthDataId1"));
            add(new Patient("2", "encryptedErika", "encryptedMusterfrau", "encryptedDate", "encryptedInsuranceNr",
                    new ContactInformation("encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown"), "encryptedHealthDataId"));
        }};

        testPatientListDecrypted = new ArrayList<>() {{
            add(new Patient("1", "Max", "Mustermann", "1999-05-16", "123495467", new ContactInformation(null, null, "Sesamstraße 56", "68593 Teststadt"), "1"));
            add(new Patient("2", "Erika", "Musterfrau", "1986-05-04", "12335467", new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"), "2"));
        }};

        when(mockDataValidationService.isValidEmail(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth(any(String.class))).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidName(anyString())).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber(anyString())).thenReturn(true);
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
        when(mockPatientRepository.findById("2")).thenReturn(Optional.ofNullable(testPatientListEncrypted.get(1)));
        Patient actual = patientService.getPatientById("2");
        verify(mockPatientRepository).findById("2");
        assertNotEquals(testPatientListEncrypted.get(1), actual);
        assertEquals(testPatientListDecrypted.get(1), actual);
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
                testPatientListDecrypted.get(1).firstname(), testPatientListDecrypted.get(1).lastname(), testPatientListDecrypted.get(1).dateOfBirth(), testPatientListDecrypted.get(1).insuranceNr(), testPatientListDecrypted.get(1).contactInformation());
        Patient expectedPatient = testPatientListEncrypted.get(1);
        HealthDataDto newHealthData = new HealthDataDto(new ArrayList<>());

        when(mockUtilService.generateId()).thenReturn("2");
        when(mockPatientRepository.save(expectedPatient)).thenReturn(expectedPatient);
        when(mockHealthDataService.createHealthData(newHealthData)).thenReturn(new HealthData("2", newHealthData.icdCodes()));
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
        PatientPersonalDTO newPatient = new PatientPersonalDTO(
                testPatientListDecrypted.get(1).firstname(), testPatientListDecrypted.get(1).lastname(),
                testPatientListDecrypted.get(1).dateOfBirth(), testPatientListDecrypted.get(1).insuranceNr(), testPatientListDecrypted.get(1).contactInformation());
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
                testPatientListDecrypted.get(1).firstname(), testPatientListDecrypted.get(1).lastname(),
                testPatientListDecrypted.get(1).dateOfBirth(), testPatientListDecrypted.get(1).insuranceNr(), testPatientListDecrypted.get(1).contactInformation());
        Patient actualPatient = testPatientListDecrypted.get(1);

        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientListEncrypted.get(1)));
        when(mockPatientRepository.save(any(Patient.class))).thenReturn(actualPatient);
        patientService.updatePatientById("2", updateDto);
        verify(mockPatientRepository).findById("2");
        verify(mockPatientRepository).save(any(Patient.class));
        assertEquals(testPatientListDecrypted.get(1), actualPatient);
        assertNotEquals(testPatientListEncrypted.get(1), actualPatient);
    }

    @Test
    void updatePatient_throwsException_whenPatientNotFound() {
        when(mockPatientRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> patientService.updatePatientById("2", new PatientPersonalDTO("Erika", "Müller", "1986-05-04", "12335467", new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt"))));
        verify(mockPatientRepository).findById("2");
    }

    @Test
    void deletePatient_deletesPatient_withGivenId() throws InvalidIdException {
        when(mockPatientRepository.existsById("2")).thenReturn(true);
        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientListEncrypted.get(1)));
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
        PatientPersonalDTO dto = new PatientPersonalDTO(
                "Max", "Mustermann", "1999-05-16", "123495467",
                new ContactInformation(null, null, "Sesamstraße 56", "68593 Teststadt")
        );

        Patient newPatient = new Patient(
                "1", "encryptedMax", "encryptedMustermann", "encryptedDate1", "encryptedInsuranceNr1",
                new ContactInformation("", "", "encryptedAddress", "encryptedTown"), "encryptedHealthDataId"
        );
        HealthDataDto newHealthData = new HealthDataDto(new ArrayList<>());


        when(mockUtilService.generateId()).thenReturn("1");
        when(mockHealthDataService.createHealthData(newHealthData)).thenReturn(new HealthData("2", newHealthData.icdCodes()));

        Patient result = patientService.createOrUpdatePatient(dto, null);

        verify(mockUtilService).generateId();
        verify(mockHealthDataService).createHealthData(newHealthData);
        assertEquals(newPatient, result);
    }

    @Test
    void createOrUpdatePatient_updatesExistingPatient_whenExistingPatientIsNotNull() {
        PatientPersonalDTO dto = new PatientPersonalDTO(
                "Max", "Mustermann", "1999-05-16", "123495467",
                new ContactInformation(null, null, "Sesamstraße 56", "68593 Teststadt")
        );
        Patient updatedPatient = new Patient(
                "1", "encryptedMax", "encryptedMustermann", "encryptedDate1", "encryptedInsuranceNr1",
                new ContactInformation("", "", "encryptedAddress", "encryptedTown"), "encryptedHealthDataId1"
        );
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
        PatientPersonalDTO validPatient = new PatientPersonalDTO(
                "Erika", "Musterfrau", "1986-05-04", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt")
        );

        // Mocked validations to return true
        when(mockDataValidationService.isValidName("Erika")).thenReturn(true);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("12335467")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);

        assertDoesNotThrow(() -> patientService.validatePatientData(validPatient));
    }

    @Test
    void validatePatientData_invalidName_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO(
                "InvalidName123", "Musterfrau", "1986-05-04", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt")
        );

        when(mockDataValidationService.isValidName("InvalidName123")).thenReturn(false);
        when(mockDataValidationService.isValidName("Musterfrau")).thenReturn(true);
        when(mockDataValidationService.isValidDateOfBirth("1986-05-04")).thenReturn(true);
        when(mockDataValidationService.isValidInsuranceNumber("12335467")).thenReturn(true);
        when(mockDataValidationService.isValidPhoneNumber("0153476539")).thenReturn(true);
        when(mockDataValidationService.isValidEmail("test@email.com")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> patientService.validatePatientData(invalidPatient)
        );

        assertEquals("Invalid name format.", thrown.getMessage());
    }

    @Test
    void validatePatientData_invalidInsuranceNumber_shouldThrowException() {
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO(
                "Erika", "Musterfrau", "1986-05-04", "invalid-insurance",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt")
        );

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
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO(
                "Erika", "Musterfrau", "invalid-date", "12335467",
                new ContactInformation("0153476539", "test@email.com", "Sesamstraße 56", "68593 Teststadt")
        );

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
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO(
                "Erika", "Musterfrau", "1986-05-04", "12335467",
                new ContactInformation("invalid-phone", "test@email.com", "Sesamstraße 56", "68593 Teststadt")
        );

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
        PatientPersonalDTO invalidPatient = new PatientPersonalDTO(
                "Erika", "Musterfrau", "1986-05-04", "12335467",
                new ContactInformation("0153476539", "invalid-email", "Sesamstraße 56", "68593 Teststadt")
        );

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
