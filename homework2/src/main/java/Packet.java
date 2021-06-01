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
    public Packet(Byte bSrc, Long bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsq = bMsq;
        wLen = bMsq.mLength();
    }
    public Packet(byte[] encodedPacket)throws BadPaddingException, IllegalBlockSizeException {
        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);
        bSrc = buffer.get();
        bPktId = buffer.getLong();
        wLen = buffer.getInt();
        wCrc16_1 = buffer.getShort();
        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());
        byte[] messageBody = new byte[wLen-8];
        buffer.get(messageBody);
        wCrc16_2 = buffer.getShort();
        bMsq.setBytes(messageBody);
        bMsq.decode();
        byte[] messageToEvaluate = new byte[wLen];
        System.arraycopy(encodedPacket, 16, messageToEvaluate, 0, wLen);
    }
    public byte[] toPacket() {
        Message message = getBMsq();
        byte[] packetPartFirst = ByteBuffer.allocate(B_MAGIC.BYTES + Byte.BYTES + Long.BYTES + Integer.BYTES)
                .put(B_MAGIC)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen)
                .array();
        wCrc16_1 = CRC16.evaluateCrc(packetPartFirst, 0, 14);
        Integer packetPartSecondLength = message.mLength();
        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message.toPacketPart())
                .array();
        wCrc16_2 = CRC16.evaluateCrc(packetPartSecond, 0, packetPartSecond.length);
        Integer packetLength = B_MAGIC.BYTES + Byte.BYTES + Long.BYTES + Integer.BYTES + wCrc16_1.BYTES + packetPartSecondLength + wCrc16_2.BYTES;
        return ByteBuffer.allocate(packetLength).put(packetPartFirst).putShort(wCrc16_1).put(packetPartSecond).putShort(wCrc16_2).array();
    }
}