import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
public class Encryptor {
    private static String generateKey = "DESede";
    private static Key key;
    private static Cipher cipher;
    static {
        try {
            key = KeyGenerator.getInstance(generateKey).generateKey();
        } catch (NoSuchAlgorithmException e) {
        }
        try {
            cipher = Cipher.getInstance(generateKey);
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        }
    }
    public synchronized static byte[] code(final byte[] message) throws BadPaddingException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch (InvalidKeyException e) {
        }
        return cipher.doFinal(message);
    }
    public synchronized static byte[] decode(final byte[] message) throws BadPaddingException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
        }
        return cipher.doFinal(message);
    }

}