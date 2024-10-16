package um_backend.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import um_backend.models.ContactInformation;
import um_backend.models.Staff;
import um_backend.models.dto.StaffDTO;
import um_backend.services.EncryptionService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StaffMapperTest {
    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private StaffMapper staffMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToEntity() {
        StaffDTO staffDTO = new StaffDTO(
                "John",
                "Doe",
                "1990-01-01",
                "Male",
                new ContactInformation("1234567890", "john.doe@example.com", "123 Main St", "Anytown"),
                "Doctor",
                "Cardiology",
                null
        );

        when(encryptionService.encrypt("John")).thenReturn("encryptedJohn");
        when(encryptionService.encrypt("Doe")).thenReturn("encryptedDoe");
        when(encryptionService.encrypt("1990-01-01")).thenReturn("encryptedDateOfBirth");
        when(encryptionService.encrypt("Male")).thenReturn("encryptedGender");
        when(encryptionService.encrypt("1234567890")).thenReturn("encryptedPhoneNr");
        when(encryptionService.encrypt("john.doe@example.com")).thenReturn("encryptedEmail");
        when(encryptionService.encrypt("123 Main St")).thenReturn("encryptedAddress");
        when(encryptionService.encrypt("Anytown")).thenReturn("encryptedTown");
        when(encryptionService.encrypt("Doctor")).thenReturn("encryptedRole");
        when(encryptionService.encrypt("Cardiology")).thenReturn("encryptedSpecialty");

        Staff staff = staffMapper.toEntity(staffDTO);

        assertEquals("encryptedJohn", staff.firstname());
        assertEquals("encryptedDoe", staff.lastname());
        assertEquals("encryptedDateOfBirth", staff.dateOfBirth());
        assertEquals("encryptedGender", staff.gender());
        assertEquals("encryptedPhoneNr", staff.contactInformation().phoneNr());
        assertEquals("encryptedEmail", staff.contactInformation().email());
        assertEquals("encryptedAddress", staff.contactInformation().address());
        assertEquals("encryptedTown", staff.contactInformation().town());
        assertEquals("encryptedRole", staff.role());
        assertEquals("encryptedSpecialty", staff.specialty());
    }

    @Test
    public void testToDto() {
        Staff staff = new Staff(
                "1",
                "encryptedJohn",
                "encryptedDoe",
                "encryptedDateOfBirth",
                "encryptedGender",
                new ContactInformation("encryptedPhoneNr", "encryptedEmail", "encryptedAddress", "encryptedTown"),
                "encryptedRole",
                "encryptedSpecialty",
                null
        );

        when(encryptionService.decrypt("encryptedJohn")).thenReturn("John");
        when(encryptionService.decrypt("encryptedDoe")).thenReturn("Doe");
        when(encryptionService.decrypt("encryptedDateOfBirth")).thenReturn("1990-01-01");
        when(encryptionService.decrypt("encryptedGender")).thenReturn("Male");
        when(encryptionService.decrypt("encryptedPhoneNr")).thenReturn("1234567890");
        when(encryptionService.decrypt("encryptedEmail")).thenReturn("john.doe@example.com");
        when(encryptionService.decrypt("encryptedAddress")).thenReturn("123 Main St");
        when(encryptionService.decrypt("encryptedTown")).thenReturn("Anytown");
        when(encryptionService.decrypt("encryptedRole")).thenReturn("Doctor");
        when(encryptionService.decrypt("encryptedSpecialty")).thenReturn("Cardiology");

        StaffDTO staffDTO = staffMapper.toDto(staff);

        assertEquals("John", staffDTO.firstname());
        assertEquals("Doe", staffDTO.lastname());
        assertEquals("1990-01-01", staffDTO.dateOfBirth());
        assertEquals("Male", staffDTO.gender());
        assertEquals("1234567890", staffDTO.contactInformation().phoneNr());
        assertEquals("john.doe@example.com", staffDTO.contactInformation().email());
        assertEquals("123 Main St", staffDTO.contactInformation().address());
        assertEquals("Anytown", staffDTO.contactInformation().town());
        assertEquals("Doctor", staffDTO.role());
        assertEquals("Cardiology", staffDTO.specialty());
    }
}
