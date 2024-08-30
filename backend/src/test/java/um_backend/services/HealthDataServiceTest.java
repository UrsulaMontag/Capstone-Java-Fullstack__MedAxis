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
    void testUpdateHealthData_CreateNew() {
        String generatedId = "newId";
        HealthData newHealthData = new HealthData(generatedId, "Female", 45, LocalDate.now(), new ArrayList<>());
        HealthDataDto healthDataDto = new HealthDataDto("Female", 45, newHealthData.firstAdmissionDate(), new ArrayList<>());

        when(mockHealthDataRepository.findById(generatedId)).thenReturn(Optional.empty());
        when(mockUtilService.generateId()).thenReturn(generatedId);

        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(newHealthData);

        healthDataService.updateHealthData(generatedId, healthDataDto);

        verify(mockHealthDataRepository).findById(generatedId);
        verify(mockUtilService).generateId();
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testUpdateHealthData_ExistingDataWithModifiedFields() {
        String dataId = "existingId";
        HealthData existingHealthData = new HealthData(dataId, "Female", 30, LocalDate.of(2020, 1, 1), new ArrayList<>());
        HealthDataDto modifiedHealthDataDto = new HealthDataDto("Male", 35, LocalDate.of(2021, 1, 1), new ArrayList<>());

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(existingHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(existingHealthData
                .withGender(modifiedHealthDataDto.gender())
                .withAgeAtFirstAdmission(modifiedHealthDataDto.ageAtFirstAdmission())
                .withFirstAdmissionDate(modifiedHealthDataDto.firstAdmissionDate())
                .withMedicalExaminations(modifiedHealthDataDto.medicalExaminations())
        );

        HealthData result = healthDataService.updateHealthData(dataId, modifiedHealthDataDto);

        assertEquals(dataId, result.id());
        assertEquals("Male", result.gender());
        assertEquals(35, result.ageAtFirstAdmission());
        assertEquals(LocalDate.of(2021, 1, 1), result.firstAdmissionDate());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testUpdateHealthData_InvalidId() {
        HealthDataDto healthDataDto = new HealthDataDto("Female", 45, LocalDate.now(), new ArrayList<>());

        String invalidId = "invalidId";

        when(mockHealthDataRepository.findById(invalidId)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> healthDataService.updateHealthData(invalidId, healthDataDto));
    }


    @Test
    void testAddOrUpdateIcdCodes_UpdateExisting() throws IllegalArgumentException {
        String dataId = "oldId";
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");

        List<MedicalExamination> medicalExaminations = new ArrayList<>() {{
            add(new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "", List.of(""),
                    List.of(new Treatment("type", "description")),
                    List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
        }};
        medicalExaminations.getLast().icdCodes().add(icdCode);
        HealthData newHealthData = new HealthData(dataId, "Female", 45, LocalDate.now(), medicalExaminations);
        HealthDataDto healthDataDto = new HealthDataDto("Female", 45, newHealthData.firstAdmissionDate(), medicalExaminations);

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(newHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.updateHealthData(dataId, healthDataDto);

        assertEquals(dataId, result.id());
        assertEquals(List.of(new IcdCode("ICD-10", "test-description")), result.medicalExaminations().getLast().icdCodes());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testAddIcdCodeToHealthData_NullIcdCode() {
        String dataId = "someId";

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(new HealthData(dataId, "Female", 45, LocalDate.now(), new ArrayList<>())));

        assertThrows(IllegalArgumentException.class, () -> healthDataService.addIcdCodeToHealthData(dataId, null));
        verify(mockHealthDataRepository, never()).save(any(HealthData.class));
    }

    @Test
    void testAddIcdCodeToHealthData_NonExistentData() {
        String dataId = "nonExistentId";
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> healthDataService.addIcdCodeToHealthData(dataId, icdCode));
        verify(mockHealthDataRepository, never()).save(any(HealthData.class));
    }

    @Test
    void testAddIcdCodeToHealthData_CatchesIllegalArgumentException() {
        String dataId = "someId";
        IcdCode icdCode = new IcdCode("ICD-10", "test-description");

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(new HealthData(dataId, "Female", 45, LocalDate.now(), new ArrayList<>())));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenThrow(new IllegalArgumentException("Underlying problem"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> healthDataService.addIcdCodeToHealthData(dataId, icdCode));
        assertEquals("Health data contains problems", exception.getMessage());
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
        List<MedicalExamination> medicalExaminations = new ArrayList<>() {{
            add(new MedicalExamination(LocalDateTime.now(), new ArrayList<>(), "", "", List.of(""),
                    List.of(new Treatment("type", "description")),
                    List.of(new VitalSigns(38, 60, 90, 120, 20)), ""));
        }};
        medicalExaminations.getLast().icdCodes().add(new IcdCode("newCode", "newDescription"));
        HealthData healthData = new HealthData("newId", "Female", 45, LocalDate.now(), medicalExaminations);
        HealthDataDto newHealthData = new HealthDataDto("Female", 45, healthData.firstAdmissionDate(), medicalExaminations);

        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(healthData);

        HealthData result = healthDataService.createHealthData(newHealthData);

        assertEquals("newId", result.id());
        assertEquals(List.of(new IcdCode("newCode", "newDescription")), result.medicalExaminations().getLast().icdCodes());
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

