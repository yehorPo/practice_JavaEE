import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Sender extends Thread {
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private Packet packet;
    private int port;
    public Sender(int port, Packet packet) {
        this.port = port;
        this.packet = packet;
    }
    @Override
    public void run() {
        Thread.currentThread().setName(Thread.currentThread().getId() + " - Sender");
        try {
            try {
                socket = new Socket("host", port);
                input = socket.getInputStream();
                output = socket.getOutputStream();
                Network network = new Network(input, output,5, TimeUnit.SECONDS);
                network.send(packet.toPacket());
                try {
                    byte[] packetBytes = network.receive();
                    Packet packet = new Packet(packetBytes);
                    System.out.println(Thread.currentThread().getName() +  " answer: " + packet.bMsq.message);
                } catch (TimeoutException e) {
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } finally {
                    input.close();
                    output.close();
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
        }

    }
}