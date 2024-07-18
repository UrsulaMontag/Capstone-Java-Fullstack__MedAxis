package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import um_backend.models.Patient;
import um_backend.models.dto.PatientPostDto;
import um_backend.repository.PatientRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
    void createPatient_returnsPatient_whenPatientIsCreated() {
        PatientPostDto newPatient = new PatientPostDto(
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
        // GIVEN
        PatientPostDto newPatient = new PatientPostDto(
                testPatientList.getFirst().firstname(), testPatientList.getFirst().lastname(), testPatientList.getFirst().dateOfBirth());
        // Mock-Objekt so konfigurieren, dass es eine Ausnahme wirft
        when(mockPatientRepository.save(any(Patient.class))).thenThrow(new NullPointerException("Error message"));
        // WHEN & THEN
        try {
            patientService.createPatient(newPatient);
            verify(mockPatientRepository).save(any(Patient.class));
            fail("Expected exception, but was not thrown");
        } catch (NullPointerException e) {
            assertEquals("Error message", e.getMessage());
        }
    }
}