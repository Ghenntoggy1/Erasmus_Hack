package test;

import org.example.Security.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionUtilsTest {

    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    @BeforeEach
    public void setUp() {
        // Manually set the encryption key for testing purposes
        encryptionUtils.base64EncryptionKey = "mF3p9G7F0zQ8uYc5WZ5f8Jv+3G1VYgNmWqv9a0z1x2E=";
        encryptionUtils.init();
    }

    @Test
    public void testEncryptDecrypt() {
        String plainText = "This is a secret message.";
        String encryptedText = encryptionUtils.encrypt(plainText);
        assertNotNull(encryptedText, "Encrypted text should not be null");

        String decryptedText = encryptionUtils.decrypt(encryptedText);
        assertEquals(plainText, decryptedText, "Decrypted text should match the original plain text");
    }

    @Test
    public void testDecryptWithInvalidCipherText() {
        String invalidCipherText = "InvalidCipherText";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            encryptionUtils.decrypt(invalidCipherText);
        });
        String expectedMessage = "Error occurred during decryption";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
