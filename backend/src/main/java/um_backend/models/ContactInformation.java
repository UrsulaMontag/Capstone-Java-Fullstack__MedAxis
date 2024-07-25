package um_backend.models;

import lombok.With;

@With
public record ContactInformation(
        String phoneNr,
        String email,
        String address,
        String town
) {
    public ContactInformation(String address, String town) {
        this(null, null, address, town);
    }
}
