package com.util.cipher;
import java.util.Base64;

public class EncryptDecrypt {
    public static String encrypt(String data)
    {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    public static String decrypt(String encryptedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        return new String(decodedBytes);
    }
}

