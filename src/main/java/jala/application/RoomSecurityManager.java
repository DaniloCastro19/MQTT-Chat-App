package jala.application;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class RoomSecurityManager {
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;


    public static SecretKey generateRoomKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public static String encrypt(String plain, SecretKey roomKey) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, roomKey, spec);
        byte[] cypherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = ByteBuffer.allocate(iv.length + cypherText.length);
        buffer.put(iv);
        buffer.put(cypherText);
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    public static String decrypt(String base64, SecretKey roomKey) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] iv = new byte[GCM_IV_LENGTH];
        buffer.get(iv);
        byte[] cypherText = new byte[buffer.remaining()];
        buffer.get(cypherText);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, roomKey, spec);
        byte[] plain = cipher.doFinal(cypherText);
        return new String(plain, StandardCharsets.UTF_8);
    }
}
