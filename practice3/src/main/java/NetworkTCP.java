import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

public class NetworkTCP {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    public NetworkTCP(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }
    public void send(byte[] msg) throws IOException {
        outputStream.write(msg);
    }
    public byte[] receive() throws IOException {
        try{
            ArrayList<Byte>     receivedBytes = new ArrayList<>(16 * 3);
            LinkedList<Integer> bMagicIndexes = new LinkedList<>();
            int    wLen    = 0;
            byte[] oneByte = new byte[1];
            byte[] packetBytes;
            while (true) {
                if (inputStream.available() == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                inputStream.read(oneByte);
                if (Packet.B_MAGIC.equals(oneByte[0]) && receivedBytes.size() > 0) {
                    bMagicIndexes.add(receivedBytes.size());
                }
                receivedBytes.add(oneByte[0]);
                if (receivedBytes.size() == 18 + wLen) {
                    final short wCrc16_2 = ByteBuffer.allocate(2).put(receivedBytes.get(receivedBytes.size() - 2))
                            .put(receivedBytes.get(receivedBytes.size() - 1)).rewind().getShort();

                    packetBytes = byteArray(receivedBytes.toArray(new Byte[0]));
                    final short crc2Evaluated =
                            CRC16.evaluateCrc(packetBytes, 16, receivedBytes.size() - 2);

                    if (wCrc16_2 == crc2Evaluated) {
                        receivedBytes.clear();
                        bMagicIndexes.clear();
                        return packetBytes;

                    } else {
                        wLen = 0;
                        receivedBytes = resetToFirstBMagic(receivedBytes, bMagicIndexes);
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
                        receivedBytes = resetToFirstBMagic(receivedBytes, bMagicIndexes);
                    }
                }
            }
        } finally {
        }
    }
    private byte[] byteArray(Byte[] objArr) {
        byte[] primitiveArr = new byte[objArr.length];

        for (int i = 0; i < objArr.length; ++i) {
            primitiveArr[i] = objArr[i];
        }
        return primitiveArr;
    }
    private ArrayList<Byte> resetToFirstBMagic(ArrayList<Byte> receivedBytes, LinkedList<Integer> bMagicIndexes) {
        if (!bMagicIndexes.isEmpty()) {
            int firstMagicByteIndex = bMagicIndexes.poll();

            ArrayList<Byte> res = new ArrayList<>(receivedBytes.size());

            for (int i = firstMagicByteIndex; i < receivedBytes.size(); ++i) {
                res.add(receivedBytes.get(i));
            }
            return res;
        } else {
            receivedBytes.clear();
            return receivedBytes;
        }
    }
    public void shutdown() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
