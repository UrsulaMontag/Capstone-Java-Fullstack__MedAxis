package um_backend.models;

import lombok.With;

@With
public record ContactInformation(
        String phoneNr,
        String email,
        String address,
        String town
) {

    public ContactInformation {
        if (phoneNr == null || phoneNr.isEmpty()) phoneNr = "";
        if (email == null || email.isEmpty()) email = "";
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        if (town == null || town.isEmpty()) {
            throw new IllegalArgumentException("Town cannot be null or empty.");
        }
    }
}
