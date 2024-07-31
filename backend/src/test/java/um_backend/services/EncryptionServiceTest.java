package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        String password = "testPassword";
        String salt = "4f6a8b2d5c3e7a1d9e8f4c2a0b1d6f5e";
        encryptionService = new EncryptionService(password, salt);
    }

    @Test
    void encrypt_ensuresChangedDataBeforeSaving() {
        String plainText = "HelloWorld";
        String encryptedText = encryptionService.encrypt(plainText);

        assertNotEquals(plainText, encryptedText);
    }

    @Test
    void decrypt_ensuresDecryptionReturnsTheOriginalText() {
        String plainText = "HelloWorld";
        String encryptedText = encryptionService.encrypt(plainText);
        String decryptedText = encryptionService.decrypt(encryptedText);

        assertEquals(plainText, decryptedText);
    }

    @Test
    void encryptDate_ensuresChangedDataBeforeSaving() {
        String date = "2024-07-30";
        String encryptedDate = encryptionService.encryptDate(date);

        assertNotEquals(date, encryptedDate);
    }

    @Test
    void decryptDate_ensuresDecryptionReturnsTheOriginalDate() {
        String date = "2024-07-30";
        String encryptedDate = encryptionService.encryptDate(date);
        String decryptedDate = encryptionService.decryptDate(encryptedDate);

        assertEquals(date, decryptedDate);
    }
}