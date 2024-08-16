package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import um_backend.exeptions.InvalidIdException;
import um_backend.models.HealthData;
import um_backend.repository.HealthDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        String icdCode = "ICD-10";
        String generatedId = "newId";

        when(mockHealthDataRepository.findById(generatedId)).thenReturn(Optional.empty());
        when(mockUtilService.generateId()).thenReturn(generatedId);

        HealthData newHealthData = new HealthData(generatedId, List.of(icdCode));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(newHealthData);

        healthDataService.addOrUpdateHealthData(generatedId, icdCode);

        verify(mockHealthDataRepository).findById(generatedId);
        verify(mockUtilService).generateId();
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testAddOrUpdateHealthData_UpdateExisting() throws IllegalArgumentException {
        String dataId = "oldId";
        String icdCode = "ICD-10";
        List<String> existingCodes = new ArrayList<>();
        existingCodes.add("existingCode");
        HealthData existingHealthData = new HealthData(dataId, existingCodes);

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(existingHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.addOrUpdateHealthData(dataId, icdCode);

        assertEquals(dataId, result.id());
        assertEquals(List.of("existingCode", icdCode), result.icdCodes());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testGetHealthDataByPatientId() throws InvalidIdException {
        String dataId = "oldId";
        HealthData healthData = new HealthData("existingId", List.of("code1"));

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(healthData));

        HealthData result = healthDataService.getHealthDataById(dataId);

        assertEquals(healthData, result);
        verify(mockHealthDataRepository).findById(dataId);
    }
}

