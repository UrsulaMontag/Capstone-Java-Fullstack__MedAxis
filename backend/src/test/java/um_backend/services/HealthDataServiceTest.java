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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testAddOrUpdateIcdCodes_CreateNew() {
        String icdCode = "ICD-10";
        String generatedId = "newId";

        when(mockHealthDataRepository.findById(generatedId)).thenReturn(Optional.empty());
        when(mockUtilService.generateId()).thenReturn(generatedId);

        HealthData newHealthData = new HealthData(generatedId, List.of(icdCode));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(newHealthData);

        healthDataService.addOrUpdateIcdCodes(generatedId, icdCode);

        verify(mockHealthDataRepository).findById(generatedId);
        verify(mockUtilService).generateId();
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testAddOrUpdateIcdCodes_InvalidId() {
        String icdCode = "ICD-10";
        String invalidId = "invalidId";

        when(mockHealthDataRepository.findById(invalidId)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> {
            healthDataService.addOrUpdateIcdCodes(invalidId, icdCode);
        });
    }

    @Test
    void testAddOrUpdateIcdCodes_UpdateExisting() throws IllegalArgumentException {
        String dataId = "oldId";
        String icdCode = "ICD-10";
        List<String> existingCodes = new ArrayList<>();
        existingCodes.add("existingCode");
        HealthData existingHealthData = new HealthData(dataId, existingCodes);

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(existingHealthData));
        when(mockHealthDataRepository.save(any(HealthData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        HealthData result = healthDataService.addOrUpdateIcdCodes(dataId, icdCode);

        assertEquals(dataId, result.id());
        assertEquals(List.of("existingCode", icdCode), result.icdCodes());
        verify(mockHealthDataRepository).findById(dataId);
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testGetHealthDataById() throws InvalidIdException {
        String dataId = "oldId";
        HealthData healthData = new HealthData("existingId", List.of("code1"));

        when(mockHealthDataRepository.findById(dataId)).thenReturn(Optional.of(healthData));

        HealthData result = healthDataService.getHealthDataById(dataId);

        assertEquals(healthData, result);
        verify(mockHealthDataRepository).findById(dataId);
    }

    @Test
    void testGetHealthDataById_InvalidId() {
        String invalidId = "invalidId";

        when(mockHealthDataRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> {
            healthDataService.getHealthDataById(invalidId);
        });
    }

    @Test
    void testCreateHealthData_Success() {
        HealthData healthData = new HealthData("newId", List.of("ICD-10"));

        when(mockHealthDataRepository.save(any(HealthData.class))).thenReturn(healthData);

        HealthData result = healthDataService.createHealthData(healthData);

        assertEquals("newId", result.id());
        assertEquals(List.of("ICD-10"), result.icdCodes());
        verify(mockHealthDataRepository).save(any(HealthData.class));
    }

    @Test
    void testCreateHealthData_NullHealthData() {
        assertThrows(IllegalArgumentException.class, () -> healthDataService.createHealthData(null),
                "HealthData cannot be null");
    }

}

