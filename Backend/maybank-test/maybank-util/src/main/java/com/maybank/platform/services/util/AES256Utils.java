package com.maybank.platform.services.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256Utils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String plainText, String key) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt(String encryptedText, String key) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = Base64.getDecoder().decode(encryptedText);
            return new String(cipher.doFinal(decryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String plainText = "username=adminuser8&password=12345678";
        String encryptStr = AES256Utils.encrypt(plainText,"8Sw1doXBThPB4G2n38DisSGu79VdWD8g");
        System.out.println("encryptStr:"+encryptStr);
        //encryptStr = "2zxK8K2/8+ORngJ3WTJWNZMMtxsSjzhn72g3w+HQrm29LBOdyHf56KAbu5oGH27E";
        String destr = AES256Utils.decrypt(encryptStr, "8Sw1doXBThPB4G2n38DisSGu79VdWD8g");
        System.out.println("destr:"+destr);
    }
}
