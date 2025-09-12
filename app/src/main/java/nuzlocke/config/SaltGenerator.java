package nuzlocke.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator {

    public static String generateSalt() {
        byte[] salt = new byte[10];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
