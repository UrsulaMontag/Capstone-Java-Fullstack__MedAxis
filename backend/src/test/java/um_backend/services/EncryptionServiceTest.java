package um_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        String password = "testPassword";
        encryptionService = new EncryptionService(password);
    }

    @Test
    void generateSalt_generatesRandomSaltKey_with32digits() {
        String salt1 = EncryptionService.generateSalt();
        String salt2 = EncryptionService.generateSalt();

        assertNotEquals(salt1, salt2);
        assertEquals(32, salt1.length());
        assertEquals(32, salt2.length());
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