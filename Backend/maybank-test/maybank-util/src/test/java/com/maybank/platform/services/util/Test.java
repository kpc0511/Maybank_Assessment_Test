package com.maybank.platform.services.util;

import java.security.SecureRandom;
import java.util.Base64;

public class Test {

    public static void main(String[] args) {
        String jwtSecret = generateJwtSecret();
        System.out.println("Generated JWT Secret: " + jwtSecret);

        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 32 bytes for 256-bit key
        secureRandom.nextBytes(key);

        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("Secret Key (Base64): " + secretKey);
    }

    private static String generateJwtSecret() {
        int byteSize = 32; // 32 bytes will result in a 64-character string

        byte[] randomBytes = new byte[byteSize];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        return Base64.getEncoder().encodeToString(randomBytes);
    }
}
