package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPersonalDTO;
import um_backend.repository.PatientRepository;

import java.time.LocalDate;
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
    private List<Patient> testPatientList;

    @BeforeEach
    void setUp() {
        mockPatientRepository = mock(PatientRepository.class);
        mockUtilService = mock(UtilService.class);
        patientService = new PatientService(mockPatientRepository, mockUtilService);
        testPatientList = new ArrayList<>() {{
            add(new Patient("1", "Max", "Mustermann", LocalDate.of(2001, 4, 12)));
            add(new Patient("2", "Erika", "Musterfrau", LocalDate.of(1986, 5, 4)));
            add(new Patient("3", "Gerlinde", "Häberle", LocalDate.of(1998, 4, 16)));
        }};
    }

    @Test
    void getAllPatients_returnsAllRegisteredPatients() {
        when(mockPatientRepository.findAll()).thenReturn(testPatientList);
        List<Patient> actual = patientService.getAllPatients();
        verify(mockPatientRepository).findAll();
        assertEquals(testPatientList, actual);
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
        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientList.get(1)));
        Patient actual = patientService.getPatientById("2");
        verify(mockPatientRepository).findById("2");
        assertEquals(testPatientList.get(1), actual);
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
                testPatientList.getFirst().firstname(), testPatientList.getFirst().lastname(), testPatientList.getFirst().dateOfBirth());
        Patient expectedPatient = testPatientList.getFirst();

        when(mockUtilService.generateId()).thenReturn("1");
        when(mockPatientRepository.save(expectedPatient)).thenReturn(expectedPatient);
        patientService.createPatient(newPatient);
        verify(mockPatientRepository).save(expectedPatient);
        verify(mockUtilService).generateId();
    }

    @Test
    void createPatient_shouldThrowException_WhenWentWrong() {
        PatientPersonalDTO newPatient = new PatientPersonalDTO(
                testPatientList.getFirst().firstname(), testPatientList.getFirst().lastname(), testPatientList.getFirst().dateOfBirth());
        when(mockPatientRepository.save(any(Patient.class))).thenThrow(new NullPointerException("Error message"));
        try {
            patientService.createPatient(newPatient);
            verify(mockPatientRepository).save(any(Patient.class));
            fail("Expected exception, but was not thrown");
        } catch (RuntimeException e) {
            assertEquals("Error message", e.getMessage());
        }
    }

    @Test
    void updatePatient_returnsPatient_whenPatientIsUpdated() throws InvalidIdException {
        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientList.get(1)));
        Patient actualPatient = patientService.updatePatientById("2", new PatientPersonalDTO("Erika", "Müller", LocalDate.of(1986, 5, 4)));
        when(mockPatientRepository.save(any(Patient.class))).thenReturn(actualPatient);
        verify(mockPatientRepository).findById("2");
        verify(mockPatientRepository).save(any(Patient.class));
        assertNotEquals(testPatientList.get(1), actualPatient);
    }

    @Test
    void updatePatient_throwsException_whenPatientNotFound() {
        when(mockPatientRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> patientService.updatePatientById("2", new PatientPersonalDTO("Erika", "Müller", LocalDate.of(1986, 5, 4))));
        verify(mockPatientRepository).findById("2");
    }

    @Test
    void deletePatient_deletesPatient_withGivenId() throws InvalidIdException {
        when(mockPatientRepository.existsById("2")).thenReturn(true);
        when(mockPatientRepository.findById("2")).thenReturn(Optional.of(testPatientList.get(1)));
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
}