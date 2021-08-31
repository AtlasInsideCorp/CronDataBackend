package com.atlasinside.crondata.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class CipherUtil {
    private static final String CLASSNAME = "CipherUtil";
    private static SecretKeySpec secretKey;
    private static final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";

    private static void setKey(String myKey) throws Exception {
        final String ctx = CLASSNAME + ".";
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new Exception(ctx + ": " + e.getMessage());
        }
    }

    public static String encrypt(String str, String secret) {
        final String ctx = CLASSNAME + ".encrypt";
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }

    public static String decrypt(String str, String secret) {
        final String ctx = CLASSNAME + ".decrypt";
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
            throw new RuntimeException(ctx + ": " + e.getMessage());
        }
    }
}
