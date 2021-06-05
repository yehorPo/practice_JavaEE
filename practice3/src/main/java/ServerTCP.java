import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerTCP extends Thread {
    public ServerSocket server;
    private final ThreadPoolExecutor executor;
    private final ThreadPoolExecutor processes;
    public ServerTCP(int port)
            throws IOException {
        super("Server");
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        processes = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        server = new ServerSocket(port);
    }
    @Override
    public void run() {
        try {
            while (true) {
                executor.execute(new SenderHandler(server.accept(), processes));
            }
        } catch (SocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            processes.shutdown();
        }
    }
    public void shutdown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}