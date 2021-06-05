import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.Socket;
import  java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public class SenderHandler implements Runnable {
    private final NetworkTCP network;
    private final ThreadPoolExecutor executor;
    private int threads = 0;
    public SenderHandler(Socket clientSocket, ThreadPoolExecutor executor) throws IOException {
        network = new NetworkTCP(clientSocket);
        this.executor = executor;
    }
    @Override
    public void run() {
        try {
            Packet helloPacket = null;
            try {
                helloPacket = new Packet((byte) 0, 0L,
                        new Message(Message.cTypes.ok, 0, "Ok"));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            assert helloPacket != null;
            network.send(helloPacket.toPacket());

            while (true) {
                byte[] packetBytes = network.receive();
                if (packetBytes == null) {
                    break;
                }
                handlePacketBytes(Arrays.copyOf(packetBytes, packetBytes.length));
            }
        } catch (IOException ignored) {
        } finally {
            shutdown();
        }
    }
    private void handlePacketBytes(byte[] packetBytes) {
        final CompletableFuture<Void> exceptionally = CompletableFuture.supplyAsync(() -> {
            Packet packet = null;
            try {
                packet = new Packet(packetBytes);
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            return packet;
        }, executor).thenAcceptAsync((inputPacket -> {
                    Packet answerPacket = null;
                    try {
                        answerPacket = ProcessHandler.process(inputPacket);
                    } catch (BadPaddingException | NullPointerException | IllegalBlockSizeException ignored) {
                    }
            try {
                assert answerPacket != null;
                network.send(answerPacket.toPacket());
                    } catch (IOException ignored) {
                    }
                    threads--;
                }), executor)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    threads--;
                    return null;
                });
    }
    public void shutdown() {
        while (threads > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        }
        network.shutdown();
    }


}