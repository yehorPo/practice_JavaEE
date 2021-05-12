import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


public class Encryptor {
    public static byte[] sendMessage(String message){
        byte[] byteMassage = message.getBytes(StandardCharsets.UTF_8);
        byte[] head = ByteBuffer.allocate(14).order(ByteOrder.BIG_ENDIAN)
            .put((byte) 0x13)
            .put((byte) 0x05)
            .putLong(1917)
            .putInt(byteMassage.length)
            .array();
        return ByteBuffer.allocate(16+byteMassage.length+2)
                .order(ByteOrder.BIG_ENDIAN)
                .put(head)
                .putShort(CRC16.crc16(head))
                .put(byteMassage)
                .putShort(CRC16.crc16(byteMassage))
                .array();

    }
}
