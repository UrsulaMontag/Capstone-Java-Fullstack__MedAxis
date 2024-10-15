package um_backend.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import um_backend.models.ContactInformation;
import um_backend.models.Staff;
import um_backend.models.dto.StaffDTO;
import um_backend.services.EncryptionService;

@Component
@RequiredArgsConstructor
public class StaffMapper {
    private final EncryptionService encryptionService;

    public Staff toEntity(StaffDTO staffDTO) {
        return new Staff(
                null,
                encryptionService.encrypt(staffDTO.firstname()),
                encryptionService.encrypt(staffDTO.lastname()),
                encryptionService.encrypt(staffDTO.dateOfBirth()),
                encryptionService.encrypt(staffDTO.gender()),
                new ContactInformation(
                        encryptionService.encrypt(staffDTO.contactInformation().phoneNr()),
                        encryptionService.encrypt(staffDTO.contactInformation().email()),
                        encryptionService.encrypt(staffDTO.contactInformation().address()),
                        encryptionService.encrypt(staffDTO.contactInformation().town())
                ),
                encryptionService.encrypt(staffDTO.role()),
                encryptionService.encrypt(staffDTO.specialty()),
                staffDTO.wards()
        );
    }

    public StaffDTO toDto(Staff entity) {
        return new StaffDTO(
                encryptionService.decrypt(entity.firstname()),
                encryptionService.decrypt(entity.lastname()),
                encryptionService.decrypt(entity.dateOfBirth()),
                encryptionService.decrypt(entity.gender()),
                new ContactInformation(
                        (!entity.contactInformation().phoneNr().isEmpty() ?
                                encryptionService.decrypt(entity.contactInformation().phoneNr()) : ""),
                        (!entity.contactInformation().email().isEmpty() ?
                                encryptionService.decrypt(entity.contactInformation().email()) : ""),
                        encryptionService.decrypt(entity.contactInformation().address()),
                        encryptionService.decrypt(entity.contactInformation().town())
                ),
                encryptionService.decrypt(entity.role()),
                encryptionService.decrypt(entity.specialty()),
                entity.wards()
        );
    }
}
