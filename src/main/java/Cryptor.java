import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
public class Cryptor {
    private static String generateKey = "QWErty";
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
    public synchronized static byte[] encryptMessage(final byte[] message) throws BadPaddingException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch (InvalidKeyException e) {
        }
        return cipher.doFinal(message);
    }
    public synchronized static byte[] decryptMessage(final byte[] message) throws BadPaddingException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
        }
        return cipher.doFinal(message);
    }

}