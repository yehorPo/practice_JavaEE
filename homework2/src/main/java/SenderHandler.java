import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
public class SenderHandler implements Runnable {
    private Socket clientSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Network network;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    public SenderHandler(Socket clientSocket, int maxTimeout, TimeUnit timeUnit) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        network = new Network(inputStream, outputStream, maxTimeout, timeUnit);
    }
    @Override
    public void run() {
        Thread.currentThread().setName(Thread.currentThread().getName() + " - SenderHandler");
        try {
            while (true) {
                byte[] packetBytes = network.receive();
                handlePacketBytes(Arrays.copyOf(packetBytes, packetBytes.length));
            }
        } catch (IOException e) {
            if (e.getMessage().equals("Stream closed.")) {
            } else {
                e.printStackTrace();
            }
        } catch (TimeoutException e) {
            System.out.println("client timeout");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }
    private void handlePacketBytes(byte[] packetBytes) {
        CompletableFuture.supplyAsync(() -> {
            Packet packet = null;
            try {
                packet = new Packet(packetBytes);
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            return packet;
        }, executor)
                .thenAcceptAsync((inputPacket -> {
                    Packet answerPacket = null;
                    try {
                        answerPacket = ProcessHandler.process(inputPacket);
                    } catch (BadPaddingException e) {
                    } catch (IllegalBlockSizeException e) {
                    } catch (NullPointerException e) {
                    }
                    try {
                        network.send(answerPacket.toPacket());
                    } catch (IOException e) {
                    }
                }), executor)
                .exceptionally(ex -> null);
    }
    public void shutdown() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        try {
            if (inputStream.available() > 0) {
                Thread.sleep(3000);
            }
            inputStream.close();
        } catch (IOException | InterruptedException e) {
        } finally {
            if (executor.getActiveCount() > 0) {
                try {
                    executor.awaitTermination(2, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                }
            }
            executor.shutdown();
            try {
                try {
                    outputStream.close();
                } finally {
                    clientSocket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}