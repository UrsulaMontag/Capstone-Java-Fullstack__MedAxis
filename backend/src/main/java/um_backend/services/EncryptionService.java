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
        String decryptedDate = decrypt(encryptedDate);
        try {
            LocalDate date = LocalDate.parse(decryptedDate, DATE_FORMATTER);
            return date.format(DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format after decryption: " + decryptedDate, e);
        }
    }

}
