package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.*;
import um_backend.models.dto.HealthDataDto;
import um_backend.repository.HealthDataRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HealthDataServiceTest {
    private HealthDataService healthDataService;

    @Mock
    private HealthDataRepository mockHealthDataRepository;
    @Mock
    private UtilService mockUtilService;

    @BeforeEach
    void setUp() {
        mockHealthDataRepository = mock(HealthDataRepository.class);
        mockUtilService = mock(UtilService.class);
        healthDataService = new HealthDataService(mockHealthDataRepository, mockUtilService);
    }

    @Test
    void testAddExaminationWithIcdCodes_Success() throws InvalidIdException {
        String dataId = "existingId";
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");
        MedicalExamination newExamination = new MedicalExamination(LocalDateTime.now(), List.of(icdCode), "", "",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "");

        HealthData existingHealthData = new HealthData(dataId, "Female", 30, LocalDate.of(2020, 1, 1),
                new ArrayList<>());
        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(existingHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.addExaminationWithIcdCodes(dataId, newExamination);

        assertEquals(dataId, result.id());
        assertEquals(1, result.medicalExaminations().size());
        assertEquals(List.of(icdCode), result.medicalExaminations().getFirst().icdCodes());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testAddExaminationWithIcdCodes_InvalidId() {
        String invalidId = "invalidId";
        MedicalExamination newExamination = new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "");

        when(mockHealthDataRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class,
                () -> healthDataService.addExaminationWithIcdCodes(invalidId, newExamination));
        verify(mockHealthDataRepository, never()).save(any(HealthData.class));
    }

    @Test
    void testUpdateHealthData_ExistingDataWithModifiedFields() throws InvalidIdException {
        String dataId = "existingId";
        HealthData existingHealthData = new HealthData(dataId, "Female", 30, LocalDate.of(2020, 1, 1),
                new ArrayList<>());
        List<MedicalExamination> medicalExaminations = new ArrayList<>() {
            {
                add(new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "", new ArrayList<>(),
                        new ArrayList<>(), new ArrayList<>(), ""));
            }
        };

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(existingHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(existingHealthData
                .withMedicalExaminations(medicalExaminations));

        HealthData result = healthDataService.updateHealthData(dataId, medicalExaminations.getFirst());

        assertEquals(dataId, result.id());
        assertEquals("Female", result.gender());
        assertEquals(30, result.ageAtFirstAdmission());
        assertEquals(LocalDate.of(2020, 1, 1), result.firstAdmissionDate());
        assertEquals(medicalExaminations, result.medicalExaminations());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testUpdateHealthData_InvalidId() {
        String invalidId = "invalidId";
        MedicalExamination newExamination = new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "");

        when(mockHealthDataRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> healthDataService.updateHealthData(invalidId, newExamination));

        verify(mockHealthDataRepository, never()).save(any(HealthData.class));
    }

    @Test
    void testAddOrUpdateIcdCodes_UpdateExisting() throws IllegalArgumentException, InvalidIdException {
        String dataId = "oldId";
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");

        List<MedicalExamination> medicalExaminations = new ArrayList<>() {
            {
                add(new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "", List.of(""),
                        List.of(new Treatment("type", "description")),
                        List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
            }
        };
        medicalExaminations.getLast().icdCodes().add(icdCode);
        HealthData newHealthData = new HealthData(dataId, "Female", 45, LocalDate.now(), medicalExaminations);

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(newHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.updateHealthData(dataId, medicalExaminations.getFirst());

        assertEquals(dataId, result.id());
        assertEquals(List.of(new IcdCode("ICD-10", "test-description")),
                result.medicalExaminations().getLast().icdCodes());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testGetHealthDataById() throws InvalidIdException {
        String dataId = "oldId";
        HealthData healthData = new HealthData(dataId, "Female", 45, LocalDate.now(), new ArrayList<>());

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(healthData));

        HealthData result = healthDataService.getHealthDataById(dataId);

        assertEquals(healthData, result);
        verify(mockHealthDataRepository).findById(dataId);
    }

    @Test
    void testGetHealthDataById_InvalidId() {
        String invalidId = "invalidId";

        when(mockHealthDataRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> healthDataService.getHealthDataById(invalidId));
    }

    @Test
    void testGetHealthDataById_EmptyData() throws InvalidIdException {
        String dataId = "emptyDataId";
        HealthData emptyHealthData = new HealthData(dataId, "Female", 45, LocalDate.now(), new ArrayList<>());

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(emptyHealthData));

        HealthData result = healthDataService.getHealthDataById(dataId);

        assertEquals(emptyHealthData, result);
        assertTrue(result.medicalExaminations().isEmpty());
        verify(mockHealthDataRepository).findById(dataId);
    }

    @Test
    void testCreateHealthData_Success() {
        List<MedicalExamination> medicalExaminations = new ArrayList<>() {
            {
                add(new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "", List.of(""),
                        List.of(new Treatment("type", "description")),
                        List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
            }
        };
        medicalExaminations.getFirst().icdCodes().add(new IcdCode("newCode", "newDescription"));
        HealthData healthData = new HealthData("newId", "Female", 45, LocalDate.now(), medicalExaminations);
        HealthDataDto newHealthData = new HealthDataDto("Female", 45, healthData.firstAdmissionDate(),
                medicalExaminations);

        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(healthData);

        HealthData result = healthDataService.createHealthData(newHealthData);

        assertEquals("newId", result.id());
        assertEquals(List.of(new IcdCode("newCode", "newDescription")), result.medicalExaminations().getFirst().icdCodes());
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testCreateHealthData_NullHealthData() {
        assertThrows(IllegalArgumentException.class, () -> healthDataService.createHealthData(null),
                "HealthData cannot be null");
    }

    @Test
    void testCreateHealthData_EmptyMedicalExaminations() {
        HealthDataDto emptyExaminationsDto = new HealthDataDto("Female", 45, LocalDate.now(), new ArrayList<>());

        when(mockUtilService.generateId()).thenReturn("newId");
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.createHealthData(emptyExaminationsDto);

        assertEquals("newId", result.id());
        assertTrue(result.medicalExaminations().isEmpty());
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

}
