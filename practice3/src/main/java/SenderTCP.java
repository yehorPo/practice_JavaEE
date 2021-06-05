import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class SenderTCP extends Thread {
    private NetworkTCP network;
    private Packet packet;
    private int waitServer = 1000;
    public SenderTCP(Packet packet) {
        this.packet = packet;
    }
    @Override
    public void run() {
        try {
            try {
                int attempt = 0;
                while (true) {
                    if (attempt == 3){
                        System.out.println("Error");
                        break;
                    };
                    initServer();
                    byte[] helloPacketBytes = network.receive();
                    if (helloPacketBytes == null) {
                        ++attempt;
                        continue;
                    }
                    Packet helloPacket = new Packet(helloPacketBytes);
                    System.out.println("Answer from sever: " +
                            helloPacket.getBMsq().getMessage());
                    network.send(packet.toPacket());
                    byte[] dataPacketBytes = network.receive();
                    if (dataPacketBytes == null) {
                        ++attempt;
                        continue;
                    }
                    Packet dataPacket = new Packet(dataPacketBytes);
                    System.out.println("Sender answer: " +
                            dataPacket.getBMsq().getMessage());
                    break;
                }
            } catch (BadPaddingException e) {
            } catch (IllegalBlockSizeException e) {
            }
        } catch (IOException e) {
        } finally {
                network.shutdown();
        }
    }private void initServer() throws IOException {
        int attempt = 0;
        while (true) {
            try {
                Socket socket = new Socket("localhost", 1);
                network = new NetworkTCP(socket);
                return;
            } catch (ConnectException e) {
                if (attempt > 3) {
                    System.out.println("Error");
                    break;
                }
                try {
                    Thread.sleep(waitServer + waitServer * attempt);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                ++attempt;
            }
        }
    }
}