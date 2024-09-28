package org.example.Security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Converter(autoApply = false)
public class CryptoAttributeConverter implements AttributeConverter<String, String> {
    private static final Logger logger = LoggerFactory.getLogger(CryptoAttributeConverter.class);
    @Autowired
    EncryptionUtils encryptionUtils;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        String encrypted = encryptionUtils.encrypt(attribute);
        logger.debug("Encrypted '{}' to '{}'", attribute, encrypted);
        return encrypted;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String decrypted = encryptionUtils.decrypt(dbData);
        logger.debug("Decrypted '{}' to '{}'", dbData, decrypted);
        return decrypted;
    }
}
