import org.junit.Test;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertThrows;

public class Tests {
    @Test
    public void test1(){
        Server server = null;
        try {
            server = new Server(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        System.out.println("==Server started==");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Packet packet1 = null;
        try {
            packet1  = new Packet((byte) 1, 1L, new Message(Message.cTypes.getProduct,1,"sender1"));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        System.out.println("==Packet inited==");
        Sender sender1 = new Sender(1, packet1);
        sender1.start();
        System.out.println("==Sender started==");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.shutdown();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==Server closed==");
    }
    @Test
     public void test2() throws IOException, BadPaddingException, IllegalBlockSizeException{
        Server server = new Server(1);
        server.start();
        Socket socket = new Socket("host", 1);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        Packet packet = new Packet((byte) 1, 1L, new Message(Message.cTypes.addGrope, 47, "client1"));
        Network network = new Network(input, output, 5, TimeUnit.SECONDS);
        byte[] corruptedPac = new byte[7];
        System.arraycopy(packet.toPacket(), 0, corruptedPac, 0, 7);
        network.send(corruptedPac);
        assertThrows(TimeoutException.class, () -> network.receive());
    }
}
