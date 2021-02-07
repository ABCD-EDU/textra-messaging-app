package preproject.backend.handlers;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * We use this class in order to generate passwords for each user. This class uses PBKDF2 with HMAC SHA-512 encryption.
 * Read information about hash encryption with salt to further understand the algorithm.
 *
 * In order to use this class to verify a certain password, use {@code verifyPassword}. When generating a new password
 * for a new user, use {@code generateHashPassword}. This class will only be used to both generate and verify passwords.
 * This prevents attacks within the database.
 *
 * To create a password, all you need is the password given by the user. This class would automatically generate the salt
 * and hash which is what you will store in the database.
 *
 * To verify a given password, you will need the password inputted by the user, the hash key from the user's account,
 * and the salt. This would prevent attacks since the only way to verify if the password is right is by rehashing the
 * given password with the salt and check if it produces the same key.
 *
 */
public class PasswordHandler {
    private static final SecureRandom RAND = new SecureRandom();

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    private static String SALT;

    public String getSalt() {
        return SALT;
    }

    public static Optional<String> generateHashPassword(String password) {
        SALT = generateSalt(); // salt that has length of 64
        return hashPassword(password, SALT);
    }

    public boolean verifyPassword (String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);
    }

    private static String generateSalt() {
        byte[] salt = new byte[64];
        RAND.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    private static Optional<String> hashPassword(String password, String salt) {
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }
}
