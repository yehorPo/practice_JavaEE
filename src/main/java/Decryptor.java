import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Decryptor {
    public static String getMessage(byte[] message){
        ByteBuffer reader = ByteBuffer.wrap(message).order(ByteOrder.BIG_ENDIAN);
        if(reader.get()!= 0x13){
            throw new IllegalArgumentException("bMagic");
        }
        byte client = reader.get();
        System.out.println("   Client: "+client);
        long numberOfMessage = reader.getLong();
        System.out.println("   Number: "+numberOfMessage);
        int messageLength = reader.getInt();
        System.out.println("   Length: "+messageLength);
        short crc16Head = reader.getShort();
        System.out.println("   Crc16 head: "+crc16Head);
        byte[] head = ByteBuffer.allocate(14).order(ByteOrder.BIG_ENDIAN)
                .put((byte) 0x13)
                .put(client)
                .putLong(numberOfMessage)
                .putInt(messageLength)
                .array();
        if(crc16Head!=CRC16.crc16(head)){
            throw new IllegalArgumentException("wCrc16");
        }
        byte[] result= Arrays.copyOfRange(message, 16, 16+messageLength);
        String text = new String(result, StandardCharsets.UTF_8);
        System.out.println("   Message: "+text);

        return null;
    }
}
