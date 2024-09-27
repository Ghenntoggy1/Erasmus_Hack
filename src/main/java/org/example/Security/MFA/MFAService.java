package org.example.Security.MFA;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class MFAService {
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private final Map<String, Boolean> scannedUsers = new ConcurrentHashMap<>();

    public String createSecret() {
        return googleAuthenticator.createCredentials().getKey();
    }
    public String generateQRCode(String secret, String username) {
        String qrCodeData = "otpauth://totp/" + username + "?secret=" + secret + "&issuer=ErasmusPP";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);

            // Convert BitMatrix to BufferedImage
            BufferedImage qrImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF); // Black or white
                }
            }

            // Convert BufferedImage to Base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] qrCodeBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes); // Return as Base64 string
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null; // Handle error appropriately
    }
    public boolean validateCode(String secret, String code) {
        return googleAuthenticator.authorize(secret, Integer.parseInt(code)); // Pass the current time in milliseconds
    }
    public boolean isCodeScanned(String username) {
        return scannedUsers.getOrDefault(username, false);
    }

    public void markAsScanned(String username) {
        scannedUsers.put(username, true); // Mark user as scanned
    }

}