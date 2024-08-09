package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import um_backend.models.HealthData;
import um_backend.repository.HealthDataRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testAddOrUpdateHealthData_CreateNew() {
        String patientId = "patient1";
        String icdCode = "ICD-10";
        String generatedId = "newId";

        when(mockHealthDataRepository.findByPatientId(patientId)).thenReturn(null);
        when(mockUtilService.generateId()).thenReturn(generatedId);

        HealthData newHealthData = new HealthData(generatedId, patientId, List.of(icdCode));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(newHealthData);

        HealthData result = healthDataService.addOrUpdateHealthData(patientId, icdCode);

        assertEquals(newHealthData, result);
        verify(mockHealthDataRepository).findByPatientId(patientId);
        verify(mockUtilService).generateId();
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testAddOrUpdateHealthData_UpdateExisting() {
        String patientId = "patient1";
        String icdCode = "ICD-10";
        List<String> existingCodes = new ArrayList<>();
        existingCodes.add("existingCode");
        HealthData existingHealthData = new HealthData("existingId", patientId, existingCodes);

        when(mockHealthDataRepository.findByPatientId(patientId)).thenReturn(existingHealthData);
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.addOrUpdateHealthData(patientId, icdCode);

        assertEquals("existingId", result.id());
        assertEquals(patientId, result.patientId());
        assertEquals(List.of("existingCode", icdCode), result.icdCodes());
        verify(mockHealthDataRepository).findByPatientId(patientId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testGetHealthDataByPatientId() {
        String patientId = "patient1";
        HealthData healthData = new HealthData("existingId", patientId, List.of("code1"));

        when(mockHealthDataRepository.findByPatientId(patientId)).thenReturn(healthData);

        HealthData result = healthDataService.getHealthDataByPatientId(patientId);

        assertEquals(healthData, result);
        verify(mockHealthDataRepository).findByPatientId(patientId);
    }
}

