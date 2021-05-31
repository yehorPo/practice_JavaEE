import java.nio.ByteBuffer;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
public class Packet {
    public final static Byte B_MAGIC = 0x13;
    Long bPktId;
    Byte bSrc;
    Integer wLen;
    Short wCrc16_1;
    Message bMsq;
    Short wCrc16_2;
    public Message getBMsq() {
        return bMsq;
    }
    public void setBMsq(Message bMsq) {
        this.bMsq = bMsq;
    }
    public Packet(Byte bSrc, Long bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsq = bMsq;
        wLen = bMsq.getLength();
    }
    public Packet(byte[] encodedPacket) throws BadPaddingException, IllegalBlockSizeException {
        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);
        Byte expectedBMagic = buffer.get();
        bSrc = buffer.get();
        bPktId = buffer.getLong();
        wLen = buffer.getInt();
        wCrc16_1 = buffer.getShort();
        final short crc16header = CRC16.evaluateCrc(encodedPacket, 0, 14);
        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());
        byte[] messageBody = new byte[wLen-8];
        buffer.get(messageBody);
        wCrc16_2 = buffer.getShort();
        bMsq.setMBytes(messageBody);
        bMsq.decode();
        byte[] messageToEvaluate = new byte[wLen];
        System.arraycopy(encodedPacket, 16, messageToEvaluate, 0, wLen);
        final short crc16message = CRC16.evaluateCrc(messageToEvaluate, 0, wLen);
    }
    public byte[] toPacket() {
        Message message = getBMsq();
        byte[] header = ByteBuffer.allocate(14)
                .put(B_MAGIC)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen)
                .array();
        wCrc16_1 = CRC16.evaluateCrc(header, 0, 14);
        Integer packetBody = message.getLength();
        byte[] packetPartSecond = ByteBuffer.allocate(packetBody)
                .put(message.inPacket())
                .array();
        wCrc16_2 = CRC16.evaluateCrc(packetPartSecond, 0, packetPartSecond.length);
        Integer packetLength = 14 + wCrc16_1.BYTES + packetBody + wCrc16_2.BYTES;
        return ByteBuffer.allocate(packetLength)
                .put(header).putShort(wCrc16_1)
                .put(packetPartSecond).putShort(wCrc16_2)
                .array();
    }
}
