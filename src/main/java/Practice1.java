import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
public class Practice1 {
    public static void main(String[] args) {
    byte[] test = Encryptor.sendMessage("Hi");
    String test1 = Decryptor.getMessage(test);
    }

}
