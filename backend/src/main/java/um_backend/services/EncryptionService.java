package um_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class EncryptionService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private final TextEncryptor textEncryptor;

    public EncryptionService(@Value("${ENCRYPTION_PASSWORD}") String password) {
        this.textEncryptor = Encryptors.text(password, generateSalt());
    }

    public static String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return new String(Hex.encode(salt));
    }

    public String encrypt(String plainText) {
        return textEncryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        return textEncryptor.decrypt(encryptedText);
    }

    public String encryptDate(String date) {
        return encrypt(date);
    }

    public String decryptDate(String encryptedDate) {
        String decryptedDate = decrypt(encryptedDate); // Entschlüsselter Wert z.B. "2001-04-12"
        try {
            LocalDate date = LocalDate.parse(decryptedDate, DATE_FORMATTER); // Sicherstellen, dass das Format korrekt ist
            return date.format(DATE_FORMATTER); // Rückgabe des Datums im gewünschten Format
        } catch (DateTimeParseException e) {
            // Behandlung des Fehlers, falls das Datum nicht korrekt geparst werden kann
            throw new IllegalArgumentException("Invalid date format after decryption: " + decryptedDate, e);
        }
    }
}
