package um_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private final TextEncryptor textEncryptor;

    public EncryptionService(@Value("${ENCRYPTION_PASSWORD}") String password, @Value("${ENCRYPTION_SALT}") String salt) {
        this.textEncryptor = Encryptors.text(password, salt);
    }

    public String encrypt(String plainText) {
        return textEncryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        return textEncryptor.decrypt(encryptedText);
    }

}
