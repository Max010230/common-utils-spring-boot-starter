package wuxingxing.me.utils.security;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

/**
 * 密码加密验证
 * Created by wind on 2017/3/22.
 */
public class Pbkdf2PasswordEncoder implements PasswordEncoder {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final int DEFAULT_HASH_WIDTH = 512;

    private static final int DEFAULT_ITERATIONS = 235000;

    private final byte[] secret;

    private final int hashWidth;

    private final int iterations;

    private BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(16);

    /**
     * Constructs a PBKDF2 password encoder with no additional security value. There will be
     * 235000 iterations and a hash width of 512. The default is based upon aiming for .5
     * seconds to validate the password when this class was added.. Users should tune
     * password verification to their own systems.
     */
    public Pbkdf2PasswordEncoder() {
        this("");
    }

    /**
     * Constructs a standard password encoder with a security value which is also included
     * in the password hash. There will be 1024 iterations and a hash width of 160.
     *
     * @param secret the security key used in the encoding process (should not be shared)
     */
    public Pbkdf2PasswordEncoder(CharSequence secret) {
        this(secret, DEFAULT_ITERATIONS, DEFAULT_HASH_WIDTH);
    }

    /**
     * Constructs a standard password encoder with a security value as well as iterations
     * and hash.
     *
     * @param secret the security
     * @param iterations the number of iterations. Users should aim for taking about .5
     * seconds on their own system.
     * @param hashWidth the size of the hash
     */
    public Pbkdf2PasswordEncoder(CharSequence secret, int iterations, int hashWidth) {
        this.secret = Utf8.encode(secret);
        this.iterations = iterations;
        this.hashWidth = hashWidth;
    }

    public void setSaltGenerator(BytesKeyGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = this.saltGenerator.generateKey();
        byte[] encoded = encode(rawPassword, salt);
        return String.valueOf(Hex.encode(encoded));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] digested = Hex.decode(encodedPassword);
        byte[] salt = subArray(digested, 0, this.saltGenerator.getKeyLength());
        return matches(digested, encode(rawPassword, salt));
    }

    /**
     * Constant time comparison to prevent against timing attacks.
     */
    private static boolean matches(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < expected.length; i++) {
            result |= expected[i] ^ actual[i];
        }
        return result == 0;
    }

    private byte[] encode(CharSequence rawPassword, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(rawPassword.toString().toCharArray(), concatenate(salt, this.secret),
                    this.iterations, this.hashWidth);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return concatenate(salt, skf.generateSecret(spec).getEncoded());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not create hash", e);
        }
    }

//    public static void main(String[] args) {
//        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
//
//        System.out.println(pbkdf2PasswordEncoder.encode("123456"));
//    }
}
