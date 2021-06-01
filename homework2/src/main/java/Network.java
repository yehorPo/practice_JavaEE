import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
public class Network {
    private BufferedOutputStream outputStream;
    private InputStream inputStream;
    private int maxTimeout;
    private TimeUnit timeUnit;
    private ArrayList<Byte> receivedBytes = new ArrayList<>(16 * 3);
    private LinkedList<Integer> bMagicIndexes = new LinkedList<>();
    private Object outputStreamLock = new Object();
    private Object inputStreamLock  = new Object();
    private byte[] byteArray(Byte[] inputArray) {
        byte[] byteArray = new byte[inputArray.length];
        for (int i = 0; i < inputArray.length; ++i) {
            byteArray[i] = inputArray[i];
        }
        return byteArray;
    }
    public Network(InputStream inputStream, OutputStream outputStream, int maxTimeout, TimeUnit timeUnit) {
        this.inputStream = inputStream;
        this.outputStream = new BufferedOutputStream(outputStream);
        this.maxTimeout = Math.max(maxTimeout, 0);
        this.timeUnit = timeUnit;
    }
    public void send(byte[] msg) throws IOException {
        synchronized (outputStreamLock) {
            outputStream.write(msg);
            outputStream.flush();
        }
    }
    private void resetToFirstBMagic() {
        if (!bMagicIndexes.isEmpty()) {
            int firstMagicByteIndex = bMagicIndexes.poll();
            ArrayList<Byte> tmp = new ArrayList<>(receivedBytes.size());
            for (int i = firstMagicByteIndex; i < receivedBytes.size(); ++i) {
                tmp.add(receivedBytes.get(i));
            }
            receivedBytes = tmp;
        } else {
            receivedBytes.clear();
        }
    }
    public byte[] receive() throws IOException, TimeoutException, BadPaddingException, IllegalBlockSizeException {
        synchronized (inputStreamLock) {
            int    wLen    = 0;
            byte[] oneByte = new byte[1];
            byte[] packetBytes;
            boolean newData = true;
            while (true) {
                if (inputStream.available() == 0) {
                    if (!newData) {
                        throw new TimeoutException();
                    }
                    newData = false;
                    try {
                        timeUnit.sleep(maxTimeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                inputStream.read(oneByte);
                newData = true;

                if (Packet.B_MAGIC.equals(oneByte[0]) && receivedBytes.size() > 0) {
                    bMagicIndexes.add(receivedBytes.size());
                }
                receivedBytes.add(oneByte[0]);
                if (receivedBytes.size() == 16 + wLen + 2) {
                    final short wCrc16_2 = ByteBuffer.allocate(2).put(receivedBytes.get(receivedBytes.size() - 2))
                            .put(receivedBytes.get(receivedBytes.size() - 1)).rewind().getShort();
                    packetBytes = byteArray(receivedBytes.toArray(new Byte[0]));
                    final short crc2Evaluated = CRC16.evaluateCrc(packetBytes, 16,
                            receivedBytes.size() - 2);

                    if (wCrc16_2 == crc2Evaluated) {
                        receivedBytes.clear();
                        bMagicIndexes.clear();
                        return packetBytes;
                    } else {
                        wLen = 0;
                        resetToFirstBMagic();
                    }
                } else if (receivedBytes.size() >= 16) {
                    final short wCrc16_1 = ByteBuffer.allocate(2).put(receivedBytes.get(14))
                            .put(receivedBytes.get(15)).rewind().getShort();
                    final short crc1Evaluated =
                            CRC16.evaluateCrc(byteArray(receivedBytes.toArray(new Byte[0])), 0, 14);
                    if (wCrc16_1 == crc1Evaluated) {
                        wLen = ByteBuffer.allocate(4).put(receivedBytes.get(10)).put(receivedBytes.get(11))
                                .put(receivedBytes.get(12)).put(receivedBytes.get(13)).rewind().getInt();
                    } else {
                        resetToFirstBMagic();
                        Packet ansPac = new Packet((byte) 0, 1L, new Message(Message.cTypes.errors,0, "Corrupted header!"));
                        send(ansPac.toPacket());
                    }
                }
            }
        }
    }

}