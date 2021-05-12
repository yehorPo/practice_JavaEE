import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class User {
    public static int NUMBER = 1;
    public static byte CLIENT;
    public static String MESSAGE;
    public static void set(byte client, int number, String message){
        NUMBER = number;
        CLIENT = client;
        MESSAGE =message;
    }
    public static void createMessage(byte client, String message) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            String encKey = "7538782F413F4428";
            byte[] keyBytes = encKey.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            int number = NUMBER++;
            set(client, number,message);
            byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
            String encryptedMessage = String.valueOf(cipher.doFinal(byteMessage));
            byte[] temp = Encryptor.sendMessage(client, number, encryptedMessage);
            System.out.println("Created new message: ");
            System.out.println("   Client: " + client);
            System.out.println("   Number: " + number);
            System.out.println("   Message: " + encryptedMessage);
            System.out.println("=========================================================================");
            System.out.println("Received new message: ");
            Decryptor.getMessage(temp);
            System.out.println("=========================================================================");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }
}

