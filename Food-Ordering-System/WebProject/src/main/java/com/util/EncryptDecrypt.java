package com.util;
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

//class EasyEncryption {
//
//
//    public static String encrypt(String data) {
//        return Base64.getEncoder().encodeToString(data.getBytes());
//    }
//
//    public static String decrypt(String encryptedText) {
//        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
//        return new String(decodedBytes);
//    }
//
//    public static void main(String[] args) {
//        String text = "Hi there";
//
//        String encryptedText = encrypt(text);
//        System.out.println("Encrypted: " + encryptedText);
//
//        String decryptedText = decrypt(encryptedText);
//        System.out.println("Decrypted: " + decryptedText);
//    }
//}
//
